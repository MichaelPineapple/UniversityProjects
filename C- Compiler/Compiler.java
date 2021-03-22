import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Compiler
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static void Global() // Global -> Type id X Global | $
    {
        if (check("int") || check("float") || check("void"))
        {
            DataType tmpA = Type();
            String tmpB = CURRENT_TOKEN.getValue();
            eggcept("id");
            X(tmpB, tmpA);
            Global();
        }
        else if (check("$"))
        {
            eggcept("$");
            if (!noMoreFunctions) error("Main function not found!");
        }
        else error("Global");
    }

    static void X(String _id, DataType _type) // X -> Decl | ( Params ) { Pre_Local }
    {
        if (check(";") || check("[")) Decl(_id, _type);
        else if (check("("))
        {
            if (noMoreFunctions) error("Last function must be 'void main(void)'!");
            currentFunctionPointer = new Function(_id, _type);
            eggcept("(");
            Params();

            if (currentFunctionPointer.getParams().size() == 0 && currentFunctionPointer.getType() == DataType.Void && currentFunctionPointer.getId().equals("main"))
                noMoreFunctions = true;

            addSymbol(currentFunctionPointer);
            eggcept(")");
            eggcept("{");
            Pre_Local();
            eggcept("}");
            gen("END", "FUNC", currentFunctionPointer.getId(), "");
            if (_type != DataType.Void && !currentFunctionPointer.getHasReturn())
                error("FUNCTION '" + _id + "' MUST HAVE A RETURN STMT!");
            currentFunctionPointer = null;
        }
        else error("X");
    }

    static void Params() // Params ->  void | Type id xParam ParamLoop
    {
        if (check("void") && (!BREAKFAST_TOKENS.get(EGGDEX + 1).getType().equals("id")))
            eggcept("void"); // SPECIAL CONDITION
        else if (check("int") || check("float") || check("void"))
        {
            DataType $type = Type();
            String $id = CURRENT_TOKEN.getValue();
            eggcept("id");
            xParam($id, $type);
            ParamLoop();
        }
        else error("Params");
    }

    static void ParamLoop() // ParamLoop -> , Type id xParam ParamLoop | e
    {
        if (check(","))
        {
            eggcept(",");
            DataType $type = Type();
            String $id = CURRENT_TOKEN.getValue();
            eggcept("id");
            xParam($id, $type);
            ParamLoop();
        }
    }

    static void xParam(String $id, DataType $type) // xParam -> [ ] | e
    {
        if (check("["))
        {
            eggcept("[");
            eggcept("]");
            $type = convertToArray($type);
        }
        Variable $newSym = new Variable($id, $type, 4);
        currentFunctionPointer.addParam($type);
        paramList.add($newSym);
    }

    static DataType Type() // Type -> int | float | void
    {
        DataType $return = DataType.Error;
        if (check("int"))
        {
            eggcept("int");
            $return = DataType.Int;
        }
        else if (check("float"))
        {
            eggcept("float");
            $return = DataType.Float;
        }
        else if (check("void"))
        {
            eggcept("void");
            $return = DataType.Void;
        }
        else error("Type");
        return $return;
    }

    static void Bullshit() // Bullshit -> Type id Decl Bullshit | e
    {
        if (check("int") || check("float") || check("void"))
        {
            DataType $type = Type();
            String $id = CURRENT_TOKEN.getValue();
            eggcept("id");
            Decl($id, $type);
            Bullshit();
        }
    }

    static void Pre_Local() // Pre_Local -> Bullshit Local
    {
        addScope();
        Bullshit();
        Local();
        removeScope();
    }

    static void Local() // Local -> Stmt Local | e
    {

        if (check("{") || check("return") || check("while") || check("if") || check("id") || check("ival") || check("fval") || check("(") || check(";"))
        {
            Stmt();
            Local();
        }
    }

    static void Stmt() // Stmt -> ExpStmt | { Pre_Local } | return ExpStmt | while ( Exp ) Stmt | if ( Exp ) Stmt Else
    {
        if (check("id") || check("ival") || check("fval") || check("(") || check(";"))
        {
            ExpStmt();
        }
        else if (check("{"))
        {
            eggcept("{");
            gen("BLOCK", "", "", "");
            Pre_Local();
            gen("END", "BLOCK", "", "");
            eggcept("}");
        }
        else if (check("return"))
        {
            eggcept("return");
            Symbol $exp = ExpStmt();
            if ($exp.getType() != currentFunctionPointer.getType())
                error("INCORRECT RETURN VALUE IN FUNCTION '" + currentFunctionPointer.getId() + "'!");
            currentFunctionPointer.setHasReturn();
            if ($exp.getType() != DataType.Void) gen("RETURN", "", "", $exp.getId());
        }
        else if (check("while"))
        {
            int $whileStart = -1;
            eggcept("while");
            eggcept("(");
            $whileStart = DINNER.size();
            Backpatch $bpA = handleQuest(Exp());
            String bpw = "bpw"+BP_NUM++;
            try { DINNER.get($whileStart).comment = "." + bpw; } catch (Exception ex) {}
            eggcept(")");
            Stmt();
            gen("BR", "", "", $whileStart + "", "jump to "+bpw);
            int val = DINNER.size();
            DINNER.get($bpA.value).C = val + "";
            BACKPATCH_BACKPACK.add(new Backpatch($bpA.name, val));
        }
        else if (check("if"))
        {
            int $bpA = -1;
            eggcept("if");
            eggcept("(");
            Backpatch bpA = handleQuest(Exp());
            eggcept(")");
            Stmt();
            Else(bpA);
        }
        else error("Stmt");
    }

    static void Decl(String $id, DataType $type) // Decl -> ; | [ ival ] ;
    {
        int $bytes = 4;
        if (check(";"))
        {
            eggcept(";");
        }
        else if (check("["))
        {
            eggcept("[");
            int $arraySize = 0;
            try
            {
                $arraySize = Integer.parseInt(CURRENT_TOKEN.getValue());
            }
            catch (Exception ex)
            {
                error("Unable to parse int!");
            }
            eggcept("ival");
            eggcept("]");
            eggcept(";");
            $type = convertToArray($type);
            $bytes = $arraySize * $bytes;
        }
        else error("Decl");
        addSymbol(new Variable($id, $type, $bytes));
    }

    static Symbol ExpStmt() // ExpStmt -> Exp ; | ;
    {
        Symbol $return = new Symbol();
        if (check(";"))
        {
            eggcept(";");
            $return = new Symbol("[null]", DataType.Void);
        }
        else if (check("id") || check("ival") || check("fval") || check("("))
        {
            $return = Exp().sym;
            eggcept(";");
        }
        else error("ExpStmt");
        return $return;
    }

    static void Else(Backpatch _bp) // Else -> else Stmt | e
    {
        if (check("else"))
        {
            eggcept("else");
            Backpatch bpB = new Backpatch(getNewBackpatch(), DINNER.size());
            gen("BR", "", "", "??", bpB.name+" = "+bpB.value);
            int val1 = DINNER.size();
            DINNER.get(_bp.value).C = val1 + "";
            BACKPATCH_BACKPACK.add(new Backpatch(_bp.name, val1));
            Stmt();
            int val2 = DINNER.size();
            DINNER.get(bpB.value).C = val2 + "";
            BACKPATCH_BACKPACK.add(new Backpatch(bpB.name, val2));
        }
        else
        {
            int val = DINNER.size();
            DINNER.get(_bp.value).C = val + "";
            BACKPATCH_BACKPACK.add(new Backpatch(_bp.name, val));
        }
    }

    static Term Exp() // Exp -> id xExp = Exp | XP
    {
        // SPECIAL LOOK AHEAD CONDITIONS
        boolean krank = true;
        int gayDepth = 0, ppDepth = 0;
        for (int i = EGGDEX; i < BREAKFAST_TOKENS.size(); i++)
        {
            String lookAhead = BREAKFAST_TOKENS.get(i).getType();
            if ("{;".contains(lookAhead) || (")".equals(lookAhead) && ppDepth < 1)) break;
            if (lookAhead.equals("[")) gayDepth++;
            else if (lookAhead.equals("]")) gayDepth--;
            else if (lookAhead.equals("(")) ppDepth++;
            else if (lookAhead.equals(")")) ppDepth--;
            else if (lookAhead.equals("=") && gayDepth == 0) krank = false;
        }
        //////////////////////////////////

        Term $return = new Term();
        if (check("(") || check("ival") || check("fval") || (check("id") && krank)) $return = XP();
        else if (check("id"))
        {
            String $id = CURRENT_TOKEN.getValue();
            eggcept("id");
            Term gay = xExp();
            SymbolType $kind = gay.symType;
            Symbol $left = checkSymbolUsage($kind, $id, new ArrayList<Symbol>() {});
            eggcept("=");
            Term $right = Exp();
            if ($left.getType() != $right.sym.getType()) error("Assign mismatch!");

            {
                String $leftVar = "[null";
                if ($kind == SymbolType.Array)
                {
                    String $index = getNewTemp();
                    $leftVar = "*" + getNewTemp();
                    if ($kind == SymbolType.Array)
                    {
                        gen("MUL", "4", gay.sym.getId(), $index);
                        gen("DISP", $index, $id, $leftVar);
                    }
                }
                else if ($kind == SymbolType.Variable) $leftVar = $left.id;


                gen("ASGN", $right.sym.getId(), "", $leftVar);
                $return = $right;
            }
        }
        else error("Exp");
        return $return;
    }

    static Term xExp() // [ Exp ] | e
    {
        Term $return = new Term(SymbolType.Variable);
        if (check("["))
        {
            eggcept("[");
            Term gay = Exp();
            if (gay.sym.getType() != DataType.Int) error("ARRAY INDEX MUST BE AN INTEGER!");
            eggcept("]");
            $return = new Term(gay.op, gay.sym, SymbolType.Array);
        }
        return $return;
    }

    static void Args(ArrayList<Symbol> _args) // Args -> Exp ArgsLoop | e
    {
        if (check("id") || check("ival") || check("fval") || check("("))
        {
            addArg(_args, Exp().sym);
            ArgsLoop(_args);
        }
    }

    static void ArgsLoop(ArrayList<Symbol> _args) // ArgsLoop -> , Exp ArgsLoop | e
    {
        if (check(","))
        {
            eggcept(",");
            addArg(_args, Exp().sym);
            ArgsLoop(_args);
        }
    }

    static Symbol Val() // Val -> id Val_prime | ival | fval
    {

        DataType $type = DataType.Error;
        String $name = "[null]";
        boolean $isArray = false;
        if (check("id"))
        {
            String $id = CURRENT_TOKEN.getValue();
            eggcept("id");
            ArrayList<Symbol> argsList = new ArrayList<>();
            Term gay = Val_prime(argsList);
            SymbolType $kind = gay.symType;
            $type = checkSymbolUsage($kind, $id, argsList).getType();

            if ($kind == SymbolType.Variable) $name = $id;
            else
            {
                String $index = getNewTemp();
                $name = getNewTemp();
                if ($kind == SymbolType.Array)
                {
                    $name = "*" + $name;
                    gen("MUL", "4", gay.sym.getId(), $index);
                    gen("DISP", $index, $id, $name);
                }
                else if ($kind == SymbolType.Function) gen("CALL", $id, argsList.size() + "", $name);
            }

        }
        else if (check("ival"))
        {
            $name = CURRENT_TOKEN.getValue();
            eggcept("ival");
            $type = DataType.Int;
        }
        else if (check("fval"))
        {
            $name = CURRENT_TOKEN.getValue();
            eggcept("fval");
            $type = DataType.Float;
        }
        else error("Value");
        return new Symbol($name, $type);
    }

    static Term Val_prime(ArrayList<Symbol> _args) // Val_prime -> [ Exp ] | ( Args ) | e
    {
        Term $return = new Term(SymbolType.Variable);
        if (check("["))
        {
            eggcept("[");
            Term gay = Exp();
            if (gay.sym.getType() != DataType.Int) error("Array index must be integer!");
            eggcept("]");
            $return = new Term(gay.op, gay.sym, SymbolType.Array);
        }
        else if (check("("))
        {
            eggcept("(");
            Args(_args);
            eggcept(")");
            $return = new Term(SymbolType.Function);
        }
        return $return;
    }

    static Term XP() // XP -> A XP_prime
    {
        Term $return = new Term();
        Symbol $t1 = _A();
        Term gay = XP_prime();
        if (gay == null) $return = new Term("q", $t1);
        else
        {
            Symbol $t2 = gay.sym;
            DataType $atype = $t1.getType(), $btype = $t2.getType();
            if ($atype == $btype)
            {
                String $temp = getNewTemp();
                gen("COMPR", $t1.getId(), $t2.getId(), $temp);
                $return = new Term(gay.op, new Symbol($temp, DataType.Int));
            }
            else error("DataType mismatch!");
        }
        return $return;
    }
    static Term XP_prime() // XP_prime -> Bool XP | e
    {
        Term $return = null;
        if (check("!=") || check("==") || check("<=") || check(">=") || check("<") || check(">")) $return = new Term(Bool(), _A());
        return $return;
    }

    static Symbol _A() // A -> M A'
    {
        Symbol m = _M();
        Symbol aPrime = _A_prime(m);
        if (aPrime == null) return m;
        else return aPrime;
    }
    static Symbol _A_prime(Symbol left) // A' -> Add M A' | e
    {
        Symbol $return = null;
        if (check("+") || check("-"))
        {
            String op = Add();
            Symbol right = _M();
            Symbol calc1 = calculate(left, right, op);
            Symbol prime2 = _A_prime(calc1);
            if (prime2 == null) $return = calc1;
            else $return = prime2;
        }
        return $return;
    }

    static Symbol _M() // M -> F M'
    {
        Symbol f = F();
        Symbol mPrime = _M_prime(f);
        if (mPrime == null) return f;
        else return mPrime;
    }
    static Symbol _M_prime(Symbol left) // M' -> Mul F M' | e
    {
        Symbol $return = null;
        if (check("*") || check("/"))
        {
            String op = Mul();
            Symbol right = F();
            Symbol calc1 = calculate(left, right, op);
            Symbol prime2 = _M_prime(calc1);
            if (prime2 == null) $return = calc1;
            else $return = prime2;
        }
        return $return;
    }

    static String Bool() // Bool -> != | == | <= | >= | < | >
    {
        String $return = "[null]";
        if (check("!="))
        {
            eggcept("!=");
            $return = "EQ";
        }
        else if (check("=="))
        {
            eggcept("==");
            $return = "NE";
        }
        else if (check("<="))
        {
            eggcept("<=");
            $return = "GT";
        }
        else if (check(">="))
        {
            eggcept(">=");
            $return = "LT";
        }
        else if (check("<"))
        {
            eggcept("<");
            $return = "GE";
        }
        else if (check(">"))
        {
            eggcept(">");
            $return = "LE";
        }
        else error("Bool");
        return $return;
    }

    static String Add() // Add -> + | -
    {
        String $return = "[null]";
        if (check("+"))
        {
            eggcept("+");
            $return = "ADD";
        }
        else if (check("-"))
        {
            eggcept("-");
            $return = "SUB";
        }
        else error("Add");
        return $return;
    }

    static String Mul() // Mul -> * | /
    {
        String $return = "[null]";
        if (check("*"))
        {
            eggcept("*");
            $return = "MUL";
        }
        else if (check("/"))
        {
            eggcept("/");
            $return = "DIV";
        }
        else error("Mul");
        return $return;
    }

    static Symbol F() // F -> Val | ( Exp )
    {
        Symbol $return = new Symbol();
        if (check("id") || check("ival") || check("fval")) $return = Val();
        else if (check("("))
        {
            eggcept("(");
            $return = Exp().sym;
            eggcept(")");
        }
        else error("F");
        return $return;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class Token
    {
        private String value, type;

        public String getValue()
        {
            return this.value;
        }

        public String getType()
        {
            return this.type;
        }

        public Token(String _value, String _type)
        {
            this.value = _value;
            this.type = _type;
        }

        public Token(String _type)
        {
            this.value = _type;
            this.type = _type;
        }

        @Override
        public String toString()
        {
            return this.type + ": " + this.value;
        }
    }

    private static class Symbol
    {
        private String id;
        private DataType type;

        public Symbol(String _id, DataType _type)
        {
            this.id = _id;
            this.type = _type;
        }

        public Symbol()
        {
            this.id = "[null]";
            this.type = DataType.Error;
        }

        public String getId()
        {
            return this.id;
        }

        public DataType getType()
        {
            return this.type;
        }

        @Override
        public String toString()
        {
            return ("Symbol(" + this.getId() + ", " + this.getType() + ")");
        }
    }

    private static class Variable extends Symbol
    {
        private int bytes = 0;

        public Variable(String _id, DataType _type, int _bytes)
        {
            super(_id, _type);
            this.bytes = _bytes;
        }

        public Variable(DataType _type)
        {
            super("[null]", _type);
        }

        public Variable()
        {
            super("[null]", DataType.Void);
        }

        @Override
        public String toString()
        {
            return ("Variable(" + this.getId() + ", " + this.getType() + ")");
        }

        public int getSize()
        {
            return this.bytes;
        }
    }

    private static class Function extends Symbol
    {
        private ArrayList<DataType> params;
        private boolean hasReturn = false;

        public Function(String _id, DataType _type, DataType... _params)
        {
            super(_id, _type);
            this.params = new ArrayList<>(Arrays.asList(_params));
        }

        public Function(String _id, DataType _type)
        {
            super(_id, _type);
            this.params = new ArrayList<>();
        }

        public void addParam(DataType _sym)
        {
            this.params.add(_sym);
        }

        public ArrayList<DataType> getParams()
        {
            return this.params;
        }

        public boolean getHasReturn()
        {
            return this.hasReturn;
        }

        public void setHasReturn()
        {
            this.hasReturn = true;
        }

        @Override
        public String toString()
        {
            return ("Function(" + this.getId() + ", " + this.getType() + ")");
        }
    }

    private static class Term
    {
        public Symbol sym;
        public String op;
        public SymbolType symType;

        public Term(String _op, Symbol _sym, SymbolType _symType)
        {
            this.sym = _sym;
            this.op = _op;
            this.symType = _symType;
        }

        public Term(SymbolType _symType)
        {
            this.sym = new Symbol();
            this.op = "[null]";
            this.symType = _symType;
        }

        public Term(String _op, Symbol _sym)
        {
            this.sym = _sym;
            this.op = _op;
            this.symType = null;
        }

        public Term()
        {
            this.sym = new Symbol();
            this.op = "[null]";
            this.symType = null;
        }
    }

    private static class Instruction
    {
        public String opCode, A, B, C, comment;

        public Instruction(String _opCode, String _a, String _b, String _c, String _comment)
        {
            opCode = _opCode;
            A = _a;
            B = _b;
            C = _c;
            comment = _comment;
        }
    }

    private static class Backpatch
    {
        public String name;
        public int value;

        public Backpatch(String _name , int _value)
        {
            this.name = _name;
            this.value = _value;
        }

    }

    enum DataType
    {Int, Float, Void, IntArray, FloatArray, Error,}

    enum SymbolType
    {Variable, Array, Function,}

    enum AcceptType
    {None, Token, Word, Int, Float, E, Error, End,}

    static void addArg(ArrayList<Symbol> _args, Symbol $temp)
    {
        _args.add(new Variable($temp.getId(), $temp.getType(), -1));
        gen("ARG", "", "", $temp.getId());
    }

    static void addSymbol(Symbol _sym)
    {
        String _id = _sym.getId();
        if (!(_sym instanceof Function) && _sym.getType() == DataType.Void)
            error("Symbol '" + _id + "' cannot be type 'void'!");
        int lastScopeIndex = LUNCH.size() - 1;
        HashMap<String, Symbol> temp = LUNCH.get(lastScopeIndex);
        if (doesSymbolAlreadyExist(_sym)) error("SYMBOL '" + _id + "' ALREADY EXISTS!");
        else
        {
            temp.put(_id, _sym);
            //if (!SHOW_ONLY_ERRORS) print("\nSYMBOL ADDED ~ " + _sym.toString() + " <Scope: "+ lastScopeIndex +">\n\n");
            if (_sym instanceof Variable)
            {
                gen("ALLOC", ((Variable) _sym).getSize() + "", "", _sym.getId());
            }
            else if (_sym instanceof Function)
            {
                Function f = ((Function) _sym);
                gen("FUNC", _sym.getId(), f.getType().toString(), f.getParams().size() + "");
                for (Variable v : paramList) gen("PARAM", "", "", v.getId());
            }
        }
    }

    static Symbol searchSymbol(String _id)
    {
        Symbol export = null;
        for (HashMap<String, Symbol> h : LUNCH) if (h.containsKey(_id)) export = h.get(_id);
        return export;
    }

    static boolean doesSymbolAlreadyExist(Symbol _sym)
    {
        boolean $return = false, isFunction = (_sym instanceof Function);
        Symbol match = LUNCH.get(LUNCH.size() - 1).get(_sym.getId());
        if (match != null)
        {
            if (isFunction && (match instanceof Function)) $return = true;
            else if (!isFunction && !(match instanceof Function)) $return = true;
        }
        return $return;
    }

    static void addScope()
    {
        LUNCH.add(new HashMap<String, Symbol>());
        for (Symbol v : paramList) addSymbol(v);
        paramList.clear();
    }

    static void removeScope()
    {
        LUNCH.remove(LUNCH.size() - 1);
    }

    static Symbol checkSymbolUsage(SymbolType usage, String _id, ArrayList<Symbol> argsList)
    {
        Symbol sym = searchSymbol(_id);
        Symbol $return = new Symbol();
        boolean found = true;
        if (sym == null)
        {
            found = false;
            error("SYMBOL '" + _id + "' NOT FOUND!");
            sym = new Symbol(_id, DataType.Error);
        }

        $return = sym;
        boolean correct = false;
        boolean correctArgs = true;
        if (sym instanceof Function)
        {
            if (usage == SymbolType.Function) correct = true;
            ArrayList<DataType> $params = ((Function) sym).getParams();
            if (argsList.size() != $params.size()) correctArgs = false;
            else for (int i = 0; i < argsList.size(); i++)
            {
                if (argsList.get(i).getType() != $params.get(i)) correctArgs = false;
            }
        }
        else if (sym instanceof Variable)
        {
            DataType dt = ((Variable) sym).getType();
            if (usage == SymbolType.Variable) correct = true;
            else if (usage == SymbolType.Array)
            {
                if (dt == DataType.IntArray || dt == DataType.FloatArray)
                {
                    correct = true;
                    Symbol $kek = $return;
                    $return = new Symbol($kek.getId(), convertFromArray($kek.getType()));
                }
            }
        }

        if (found)
        {
            if (!correct) error("SYMBOL '" + _id + "' USED INCORRECTLY!");
            if (!correctArgs) error("INCORRECT ARGUMENTS FOR FUNCTION '" + _id + "'!");
        }

        return $return;
    }

    static boolean isArray(DataType _type)
    {
        return (_type == DataType.IntArray || _type == DataType.FloatArray);
    }

    static DataType convertToArray(DataType _type)
    {
        if (_type == DataType.Int) return DataType.IntArray;
        else if (_type == DataType.Float) return DataType.FloatArray;
        else return _type;
    }

    static DataType convertFromArray(DataType _type)
    {
        if (_type == DataType.IntArray) return DataType.Int;
        else if (_type == DataType.FloatArray) return DataType.Float;
        else return _type;
    }

    static Symbol calculate(Symbol a, Symbol b, String op)
    {
        DataType aType = a.getType(), bType = b.getType();

        if (isArray(aType) || isArray(bType))
        {
            error("Cannot perform arithmatic with arrays!");
            return new Symbol("[null_id]", DataType.Error);
        }
        else if (aType == DataType.Void || bType == DataType.Void)
        {
            error("Cannot perform arithmatic with 'void'!");
            return new Symbol("[null_id]", DataType.Error);
        }
        else if (aType == bType)
        {
            String t = getNewTemp();
            gen(op, a.getId(), b.getId(), t);
            return new Symbol(t, b.getType());
        }
        else
        {
            error("DataType Mismatch!");
            return new Symbol("[null_id]", DataType.Error);
        }
    }

    static Backpatch handleQuest(Term _in)
    {
        if (_in.op.equals("q"))
        {
            String $t = getNewTemp();
            gen("COMPR", _in.sym.getId(), "1", $t);
            int out = DINNER.size();
            String bpName = getNewBackpatch();
            gen("BREQ", $t, "", "??", bpName + " = " + out);
            return new Backpatch(bpName, out);
        }
        else
        {
            int out = DINNER.size();
            String bpName = getNewBackpatch();
            gen("BR" + _in.op, _in.sym.getId(), "", "??", bpName+" = "+out);
            return new Backpatch(bpName, out);
        }
    }

    static void print(String txt)
    {
        //if (!TEST_MODE) System.out.print(txt);
        System.out.print(txt);
    }

    static void gen(String _op, String _a, String _b, String _c, String _comment)
    {
        DINNER.add(new Instruction(_op, _a, _b, _c, "."+_comment));
    }
    static void gen(String _op, String _a, String _b, String _c)
    {
        DINNER.add(new Instruction(_op, _a, _b, _c, ""));
    }

    static String getNewTemp()
    {
        String $return = "t" + TEMP_NUM;
        TEMP_NUM++;
        return $return;
    }

    static String getNewBackpatch()
    {
        String $return = "bp" + BP_NUM;
        BP_NUM++;
        return $return;
    }

    static boolean check(String token)
    {
        return token.equals(CURRENT_TOKEN.getType());
    }

    static void error(String txt)
    {
        print("\nCOMPILE ERROR: " + txt + "");
        reject = true;
    }

    static void eggcept(String token)
    {
        if (check(token))
        {
            //if (!SHOW_ONLY_ERRORS) print(CURRENT_TOKEN.getValue() + " ");
        }
        else error("expected " + token + " found " + CURRENT_TOKEN.getValue());
        EGGDEX++;
        if (EGGDEX < BREAKFAST_TOKENS.size()) CURRENT_TOKEN = BREAKFAST_TOKENS.get(EGGDEX);
        //else print("\nEND");
    }

    static ArrayList<Token> BREAKFAST_TOKENS;
    static ArrayList<HashMap<String, Symbol>> LUNCH;
    static ArrayList<Instruction> DINNER;
    static ArrayList<Variable> paramList;
    static ArrayList<Backpatch> BACKPATCH_BACKPACK;
    static Token CURRENT_TOKEN;
    static Function currentFunctionPointer;
    static int EGGDEX, TEMP_NUM, BP_NUM;
    static boolean reject, noMoreFunctions;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args)
    {
        compile(args);
    }

    public static void compile(String[] args)
    {
        //print("============================================================\n");

        // Initialize Variables
        BREAKFAST_TOKENS = new ArrayList<>();
        LUNCH = new ArrayList<>();
        DINNER = new ArrayList<>();
        DINNER.add(new Instruction("-", "-", "-", "-", "-"));
        EGGDEX = 0;
        TEMP_NUM = 0;
        BP_NUM = 0;
        CURRENT_TOKEN = null;
        reject = false;
        paramList = new ArrayList<>();
        currentFunctionPointer = null;
        noMoreFunctions = false;
        BACKPATCH_BACKPACK = new ArrayList<>();

        if (args.length > 0)
        {
            String filename = args[0];
            //print("\nFILE: " + filename + "\n");
            String sourceCode = "";
            try
            {
                FileReader reader = new FileReader(new File(args[0]));
                BufferedReader fb = new BufferedReader(reader);
                String line;
                while ((line = fb.readLine()) != null) sourceCode += (line.replace("\n", "") + "\n");
            }
            catch (Exception ex) { System.out.println("\nError: " + ex.getMessage()); }

            //print("\n==============\n***インプット***\n==============\n\n" + sourceCode);
            ArrayList<Token> breakfast = lex(sourceCode);
            addScope();
            BREAKFAST_TOKENS = breakfast;
            CURRENT_TOKEN = BREAKFAST_TOKENS.get(EGGDEX);
            Global();
            print("\n");
            if (reject) System.out.println("REJECT");
            else System.out.println("ACCEPT");
            //print("\n================\n***アウトプット***\n================\n");
            print("\n================\n***CODE GEN***\n================\n");
            try
            {
                for (int i = 0; i < BACKPATCH_BACKPACK.size(); i++)
                {
                    Backpatch bp = BACKPATCH_BACKPACK.get(i);
                    DINNER.get(bp.value).comment = ".val for " + bp.name;
                }
            }
            catch (Exception ex) {}
            for (int i = 1; i < DINNER.size(); i++)
            {
                Instruction a = DINNER.get(i);
                System.out.printf("%03d\t\t%-10s %-10s %-10s %-10s   %s\n", i, a.opCode, a.A, a.B, a.C, a.comment);
            }
        }
        else System.out.println("\nNeed Command Line Argument!");

        //print("\n\n============================================================");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /***LEX STATE MACHINE***/
    static ArrayList<Token> lex(String _source)
    {
        ArrayList<Token> export = new ArrayList<>();
        int state = 0, nextState = 0, commentDepth = 0;
        AcceptType accept = AcceptType.None;
        String walk = "", code = _source;
        code += "\0";

        for (int i = 0; i < code.length(); i++)
        {
            char c = code.charAt(i);
            state = nextState;
            accept = AcceptType.None;

            switch (state)
            {
                case 0:
                    walk = "";
                    if (c == '\0') accept = AcceptType.End;
                    else if (c == ' ' || c == '\n' || c == '\t') nextState = 0;
                    else if (c == '=' || c == '<' || c == '>') nextState = 1;
                    else if (c == '!') nextState = 3;
                    else if (c == '/') nextState = 9;
                    else if ("+-()[]{};,*".contains(c + "")) nextState = 2;
                    else if (Character.isDigit(c)) nextState = 6;
                    else if (Character.isAlphabetic(c)) nextState = 4;
                    else nextState = -1;
                    break;

                case -1:
                    accept = AcceptType.Error;
                    break;

                case 1:
                    if (c == '=') nextState = 2;
                    else accept = AcceptType.Token;
                    break;

                case 2:
                    accept = AcceptType.Token;
                    break;

                case 3:
                    if (c == '=') nextState = 2;
                    else accept = AcceptType.Error;
                    break;

                case 4:
                    if (Character.isAlphabetic(c)) nextState = 4;
                    else accept = AcceptType.Word;
                    break;

                case 6:
                    if (Character.isDigit(c)) nextState = 6;
                    else if (c == '.') nextState = 7;
                    else if (c == 'E') nextState = 14;
                    else accept = AcceptType.Int;
                    break;

                case 7:
                    if (Character.isDigit(c)) nextState = 8;
                    else accept = AcceptType.Error;
                    break;

                case 8:
                    if (Character.isDigit(c)) nextState = 8;
                    else if (c == 'E') nextState = 14;
                    else accept = AcceptType.Float;
                    break;

                case 9:
                    if (c == '/') nextState = 10;
                    else if (c == '*')
                    {
                        nextState = 11;
                        commentDepth++;
                    }
                    else accept = AcceptType.Token;
                    break;

                case 10:
                    if (c == '\n') nextState = 0;
                    else nextState = 10;
                    break;

                case 11:
                    if (c == '/') nextState = 12;
                    else if (c == '*') nextState = 13;
                    else nextState = 11;
                    break;

                case 12:
                    if (c == '*')
                    {
                        nextState = 11;
                        commentDepth++;
                    }
                    else nextState = 11;
                    break;

                case 13:
                    if (c == '/')
                    {
                        commentDepth--;
                        if (commentDepth > 0) nextState = 11;
                        else nextState = 0;
                    }
                    else if (c == '*') nextState = 13;
                    else nextState = 11;
                    break;

                case 14:
                    if (c == '+' || c == '-') nextState = 15;
                    else if (Character.isDigit(c)) nextState = 16;
                    else accept = AcceptType.Error;
                    break;

                case 15:
                    if (Character.isDigit(c)) nextState = 16;
                    else accept = AcceptType.Error;
                    break;

                case 16:
                    if (Character.isDigit(c)) nextState = 16;
                    else accept = AcceptType.E;
                    break;


            }

            if (accept != AcceptType.None)
            {
                Token soup = new Token("[null]");
                if (accept == AcceptType.End) soup = new Token("$");
                else
                {
                    if (accept == AcceptType.Word)
                    {
                        if (walk.equals("int") || walk.equals("float") || walk.equals("void") || walk.equals("if") || walk.equals("else") || walk.equals("while") || walk.equals("return"))
                        {
                            soup = new Token(walk);
                        }
                        else soup = new Token(walk, "id");
                    }
                    else if (accept == AcceptType.Error) error("EGG1");
                    else if (accept == AcceptType.Float || accept == AcceptType.E) soup = new Token(walk, "fval");
                    else if (accept == AcceptType.Int) soup = new Token(walk, "ival");
                    else soup = new Token(walk);

                    //print(walk + "\n");
                    i--;
                    nextState = 0;
                }
                export.add(soup);
            }
            walk += c;
        }


        return export;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}