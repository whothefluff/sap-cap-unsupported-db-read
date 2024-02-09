package customer.sap_cap_unsupported_db_read.externalcatalogservice.jpql;

import static com.sap.cds.ql.cqn.CqnConnectivePredicate.Operator.AND;
import static java.util.stream.Collectors.joining;
import static lombok.AccessLevel.PROTECTED;

import java.util.Locale;
import java.util.function.BiFunction;

import com.sap.cds.impl.builder.model.Disjunction;
import com.sap.cds.impl.util.Stack;
import com.sap.cds.jdbc.generic.GenericFunctionMapper;
import com.sap.cds.ql.cqn.CqnArithmeticExpression;
import com.sap.cds.ql.cqn.CqnBooleanLiteral;
import com.sap.cds.ql.cqn.CqnComparisonPredicate;
import com.sap.cds.ql.cqn.CqnConnectivePredicate;
import com.sap.cds.ql.cqn.CqnElementRef;
import com.sap.cds.ql.cqn.CqnFunc;
import com.sap.cds.ql.cqn.CqnInPredicate;
import com.sap.cds.ql.cqn.CqnListValue;
import com.sap.cds.ql.cqn.CqnNegation;
import com.sap.cds.ql.cqn.CqnNullValue;
import com.sap.cds.ql.cqn.CqnNumericLiteral;
import com.sap.cds.ql.cqn.CqnPredicate;
import com.sap.cds.ql.cqn.CqnSelectListItem;
import com.sap.cds.ql.cqn.CqnSortSpecification;
import com.sap.cds.ql.cqn.CqnStringLiteral;
import com.sap.cds.ql.cqn.CqnStructuredTypeRef;
import com.sap.cds.ql.cqn.CqnToken;
import com.sap.cds.ql.impl.Xpr;

import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Accessors( chain = true,
            fluent = true )
@FieldDefaults( level = PROTECTED,
                makeFinal = true )
public class CqnVisitor implements com.sap.cds.ql.cqn.CqnVisitor {

    private static char SQ = '\'';
    private static int SUBSTRING_START_PARAM = 1;
    private static String SQL_TRUE = "TRUE";
    private static String SQL_FALSE = "FALSE";
    Stack<String> stack = new Stack<>( );

    @Override
    public void visit( final CqnSelectListItem sli ){

        if( sli.isRef( ) ){
            this.stack.add( sli.asRef( ).displayName( ) );
        } else if( sli.isValue( ) ){ 
            this.visit( ( CqnFunc )sli.asValue( ).value( ) );
        } else{
            throw new RuntimeException( "Unknown select list item type: " + sli.getClass( ) );
        }

    }

    @Override
	public void visit( final CqnStructuredTypeRef ref ){

		ref.rootSegment( ).filter( ).ifPresent( f -> f.accept( this ) );

	}

    @Override
    public void visit( final CqnElementRef ref ){ 

        this.stack.push( ref.displayName( ) ); 

    }

    @Override
    public void visit( final CqnListValue listValue ){

        val n = ( int )listValue.values( ).count( );
        val sql = this.stack.pop( n ).stream( ).collect( joining( ", ", "(", ")" ) );
        this.stack.push( sql );

    }

    @Override
    public void visit( final CqnComparisonPredicate comparison ){   

        val right = this.stack.pop( );
        val left = this.stack.pop( );
        val operatorSymbol = comparison.operator( ).symbol;
        String operator;
        switch( operatorSymbol ){
            case "is" -> operator = "=";
            case "is not" -> operator = "<>";
            default -> operator = operatorSymbol;
        }
        this.stack.push( left + " " + operator + " " + right ); 

    }

    @Override
    public void visit( final CqnNullValue nil ){

        this.stack.push("NULL");

    }

    @Override
    public void visit( final CqnSortSpecification sort ){ 

        sort.value( ).accept( this );
        val field = this.stack.pop( );
        this.stack.push( field + " " + sort.order( ).name( ) );

    }

    @Override
    public void visit( final CqnStringLiteral literal ){  

        this.stack.push( literal( literal.value( ) ) );

    }

    @Override
    public void visit( final CqnFunc cqnFunc ){

        val func = cqnFunc.func( ).toLowerCase( Locale.US );
        val args = this.stack.pop( cqnFunc.args( ).size( ) );
        if( "substring".equals( func ) ){
            // increment the start pos as OData and CQN start at 0 but SQL starts at 1
            args.set( SUBSTRING_START_PARAM, args.get( SUBSTRING_START_PARAM ) + " + 1" );
        }
        this.stack.push( new GenericFunctionMapper( ).toSql( func, args ) );

    }

    @Override
    public void visit(final CqnBooleanLiteral bool) {

        this.stack.push( Boolean.TRUE.equals( bool.value( ) ) ? SQL_TRUE : SQL_FALSE );

    }

    @Override
    public void visit( final CqnNumericLiteral<?> number ){

        val val = number.value( );
        if( number.isConstant( ) && this.isNonDecimal( val ) ){
            this.stack.push( String.valueOf( val ) );
        } else {
            throw new UnsupportedOperationException( "Unsupported numeric literal: " + number.toJson( ) );
        }

    }

    @Override
    public void visit( final CqnArithmeticExpression expr ){

        val right = this.stack.pop( );
        val left = this.stack.pop( );
        this.stack.push( "(" + left + " " + expr.operator( ).symbol( ) + " " + right + ")" );

    }

    @Override
    public void visit( final CqnInPredicate in ){

        val valueSet = this.stack.pop( );
        val value = this.stack.pop( );
        this.stack.push( value + " in " + valueSet );

    }

    @Override
    public void visit( final CqnNegation neg ){

        if( neg.predicate( ) instanceof CqnConnectivePredicate ){
            this.stack.push( "not (" + this.stack.pop( ) + ")" );
        } else{
            this.stack.push( "not " + this.stack.pop( ) );
        }

    }

    @Override
    public void visit( final CqnConnectivePredicate connective ){

        val symbol = connective.operator( ).symbol;
        final BiFunction<CqnPredicate, String, String> flattener = connective.operator( ) == AND ? this::flatAnd 
                                                                                                 : this::flatOr;
        val original = connective.predicates( );
        val snippets = this.stack.pop( original.size( ) ); 
        val sql = new StringBuilder( );
        for( int i = 0; i < original.size( ); i++ ){
            if( i > 0 ){
                sql.append( " " );
                sql.append( symbol );
                sql.append( " " );
            }
            sql.append( flattener.apply( original.get( i ), snippets.get( i ) ) );
        }
        this.stack.push( sql.toString( ) );

    }

    public static String literal( final String text ){

        return text == null ? "NULL" 
                            : SQ + prefix( text, SQ, SQ ) + SQ;

    }

    private boolean isNonDecimal( final Number number ){

        return number instanceof Integer || number instanceof Long || number instanceof Short;

    }

   
    public String apply( final CqnToken pred ){

        if( pred == null ){
            throw new IllegalArgumentException( "predicate must not be null" );
        }
        val visitor = new CqnVisitor( );
        pred.accept( visitor );
        String sql = visitor.get( pred );
        if( pred instanceof Xpr ){
            sql = sql.substring( 1, sql.length( ) - 1 );
        }
        return sql;

    }

    private String get( final CqnToken pred ){

        if( this.stack.size( ) != 1 ){
            throw new IllegalStateException( "token " + pred.toJson( ) + " can't be mapped" );
        }
        return this.stack.pop( );

    }

    private static String prefix( final String text, final char escapeChar, final char toBeEscaped ){

        for( int i = 0; i < text.length( ); i++ ){
            if( text.charAt( i ) == toBeEscaped ){
                return prefixFrom( text, i, escapeChar, text.length( ), toBeEscaped );
            }
        }
        return text;

    }

    private static String prefixFrom( final String text, final int from, final char escapeChar, final int length, final char toBeEscaped ){
        
        val buffer = new char[length * 2];
        text.getChars( 0, from, buffer, 0 );
        var j = from;
        for( int i = from; i < length; i++ ){
            val c = text.charAt( i );
            if( c == toBeEscaped ){
                buffer[j++] = escapeChar;
            }
            buffer[j++] = c;
        }
        return String.copyValueOf( buffer, 0, j );

    }

    private String flatAnd( final CqnPredicate pred, final String snippet ){

        if( pred instanceof Disjunction ){
            return "(" + snippet + ")"; 
        }
        return snippet;

    }

    String flatOr( final CqnPredicate pred, final String snippet ){

        return snippet;

    }

}