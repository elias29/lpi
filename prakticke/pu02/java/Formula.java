import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public abstract class Formula {
	ArrayList<Formula> zloz= new ArrayList<>();
	public Formula[] subf(){
		Formula vysl[] = new Formula[zloz.size()];
		for (int i = 0; i < vysl.length; i++) {
			vysl[i] = zloz.get(i);
		}
		return vysl;
	}
	public abstract String toString();
	public abstract boolean isSatisfied(Map<String,Boolean> v);
	public boolean equals(Formula other) {
		return this.toString().equals(other.toString());
	}
	
	public abstract int deg();
	public Set<String> vars(){
		Set<String> vysl = new HashSet<>();
		for (Formula f : zloz) {
			vysl.addAll(f.vars());
		}
		return vysl;
	}
	public abstract Formula substitute(Formula what, Formula replacement);
	public abstract Formula copy();
	

}

class Variable extends Formula{
	String name = "";
	
	public Set<String> vars(){
		Set<String> vysl = new HashSet<>();
		vysl.add(name);
		return vysl;
	}
	public Variable(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	} 
	
	public String name() {
		return name;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		return v.get(name);
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if(this.equals(what)) {
			return replacement.copy();
		}
		// TODO Auto-generated method stub
		return this.copy();
	}

	@Override
	public Formula copy() {
		// TODO Auto-generated method stub
		return new Variable(name);
	}

}
class Negation extends Formula{
	 public Negation(Formula formula) {
		 zloz.add(formula);
		// TODO Auto-generated constructor stub
	}
	public Formula originalFormula() {
		return zloz.get(0);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return '-' + originalFormula().toString();
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		return !originalFormula().isSatisfied(v);
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		return originalFormula().deg() + 1;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if (this.equals(what)) {
			return replacement.copy();
		}
		// TODO Auto-generated method stub
		return new Negation(originalFormula().substitute(what, replacement));
	}

	@Override
	public Formula copy() {
		// TODO Auto-generated method stub
		return new Negation(originalFormula().copy());
	}
	
}
class Disjunction extends Formula{
	public Disjunction(Formula formula[]) {
		for (int i = 0; i < formula.length; i++) {
			zloz.add(formula[i]);
		}
	}
	@Override
	public String toString() {
		String vysl = "";
		for(Formula formula : zloz) {
			vysl += formula;
			vysl += '|';
		}
		vysl = vysl.substring(0, vysl.length()-1);
				
		// TODO Auto-generated method stub
		return "(" + vysl + ')';
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		for(Formula formula : zloz) {
			if (formula.isSatisfied(v)) {
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int deg() {
		int sucet = 0;
		for(Formula formula : zloz) {
			sucet += formula.deg() ;
		}
		// TODO Auto-generated method stub
		return sucet + 1;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if(this.equals(what)) {
			return replacement.copy();
		}
		Formula upravene[] = new Formula[zloz.size()];
		for(int i = 0; i < zloz.size(); i++) {
			upravene[i] = zloz.get(i).substitute(what, replacement);
		}
		// TODO Auto-generated method stub
		return new Disjunction(upravene);
	}
	public Formula copy() {
		Formula upravene[] = new Formula[zloz.size()];
		for(int i = 0; i < zloz.size(); i++) {
			upravene[i] = zloz.get(i).copy();
		}
		// TODO Auto-generated method stub
		return new Disjunction(upravene);
	}
	
}
class Conjunction extends Formula{
	public Conjunction(Formula formula[]) {
		for (int i = 0; i < formula.length; i++) {
			zloz.add(formula[i]);
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String vysl = "";
		for(Formula formula : zloz) {
			vysl += formula;
			vysl += '&';
		}
		vysl = vysl.substring(0, vysl.length()-1);
				
		// TODO Auto-generated method stub
		return '(' + vysl + ')';
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		for(Formula formula : zloz) {
			if (formula.isSatisfied(v) == false) {
				return false;
			}
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int deg() {
		int sucet = 0;
		for(Formula formula : zloz) {
			sucet += formula.deg();
		}
		// TODO Auto-generated method stub
		return sucet + 1;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if(this.equals(what)) {
			return replacement.copy();
		}
		Formula upravene[] = new Formula[zloz.size()];
		for(int i = 0; i < zloz.size(); i++) {
			upravene[i] = zloz.get(i).substitute(what, replacement);
		}
		// TODO Auto-generated method stub
		return new Conjunction(upravene);
	}

	@Override
	public Formula copy() {
		Formula upravene[] = new Formula[zloz.size()];
		for(int i = 0; i < zloz.size(); i++) {
			upravene[i] = zloz.get(i).copy();
		}
		// TODO Auto-generated method stub
		return new Conjunction(upravene);
	}
	
}

abstract class BinaryFormula extends Formula{
	Formula lava;
	Formula prava;
	public Set<String> vars(){
		Set<String> vysl = new HashSet<>();
		vysl.addAll(lava.vars());
		vysl.addAll(prava.vars());
		return vysl;
	}
	@Override
	
	public int deg() {
		// TODO Auto-generated method stub
		return lava.deg() + prava.deg() + 1;
	}

	public Formula lava() {
		return lava;
	}
	public Formula prava() {
		return prava;
	}
}
class Implication extends BinaryFormula{
	public Implication(Formula formula1 , Formula formula2) {
		lava = formula1;
		prava = formula2;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return '(' + lava.toString() + "->" + prava.toString() + ')';
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		if(lava.isSatisfied(v) && !prava.isSatisfied(v)) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Formula copy() {
		// TODO Auto-generated method stub
		return new Implication(lava.copy(), prava.copy());
	}
	public Formula leftSide() {
		return lava;
	}
	public Formula rightSide() {
		return prava;
	}
	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if(this.equals(what)) {
			return replacement.copy();
		}
		// TODO Auto-generated method stub
		return new Implication(lava.substitute(what, replacement), prava.substitute(what, replacement));
	}
	
}
class Equivalence extends BinaryFormula{
	public Equivalence(Formula formula1 , Formula formula2) {
		// TODO Auto-generated constructor stub
		lava = formula1;
		prava = formula2;
	}
	public Formula leftSide() {
		return lava;
	}
	public Formula rightSide() {
		return prava;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return '(' + lava.toString() + "<->" + prava.toString() + ')';
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		if(lava.isSatisfied(v) == prava.isSatisfied(v)) {
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Formula copy() {
		// TODO Auto-generated method stub
		return new Equivalence(lava.copy(), prava.copy());
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		if(this.equals(what)) {
			return replacement.copy();
		}
		// TODO Auto-generated method stub
		return new Equivalence(lava.substitute(what, replacement), prava.substitute(what, replacement));
	}
	
}
