package RexJgg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TIndividual {
	private double fEvaluationValue;
	private TVector fVector;
	public TIndividual() {
		fEvaluationValue = Double.NaN;
		fVector = new TVector();
	}
	public TIndividual(TIndividual src) {
		fEvaluationValue = src.fEvaluationValue;
		fVector = new TVector(src.fVector);
	}
	public TIndividual copyFrom(TIndividual src) {
		fEvaluationValue = src.fEvaluationValue;
		fVector.copyFrom(src.fVector);
		return this;
	}
	@Override
	public TIndividual clone() {
		return new TIndividual(this);
	}
	@Override
	public String toString() {
		String str = fEvaluationValue + "\n";
		str += fVector.toString();
		return str;
	}
	public void writeTo(PrintWriter pw) {
		pw.println(fEvaluationValue);
		fVector.writeTo(pw);
	}
	public void readFrom(BufferedReader br) throws IOException{
		fEvaluationValue = Double.parseDouble(br.readLine());
		fVector.readFrom(br);
	}
	public double getEValuationValue() {
		return fEvaluationValue;
	}
	public void setEvaluationValue(double eval) {
		fEvaluationValue = eval;
	}
	public TVector getVector() {
		return fVector;
	}

	//テストコード
	public static void main(String[] args) {
		TIndividual tindividual = new TIndividual();
		tindividual.fVector.setDimension(5);
		for(int i=0;i<tindividual.fVector.getDimension();i++) {
			double x = 1.0 * (i + 1);
			tindividual.fVector.setElement(i, x);
		}
		double eval = 20.0;
		tindividual.setEvaluationValue(eval);

		System.out.println(tindividual.getEValuationValue());
		System.out.println(tindividual.getVector());


	}


}
