package RexJgg;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class TRex {
	private Random fRandom;

	public TRex(Random random) {
		fRandom = random;
	}

	//子個体の生成
	/***
	 * @param
	 * parents :親個体
	 * noOfOffspring : 生成子個体数
	 */
	public TPopulation makeOffspring(TPopulation parents, int noOfOffspring) {

		int parentsSize = parents.getPopulationSize();
		int dimension = parents.getIndividual(0).getVector().getDimension();
		TKTablet ktablet = new TKTablet(dimension / 4, dimension);

		//重心<y>の生成
		TVector yMean = new TVector();
		yMean.setDimension(dimension);

		//<y> を0に初期化
		for(int i = 0; i < dimension; ++i) {
			yMean.setElement(i, 0);
		}

		//<y>の計算
		for(int i = 0; i < parentsSize; ++i) {
			yMean.add(parents.getIndividual(i).getVector());
		}
		yMean.scalarProduct(1.0 / (double)parentsSize);

		//σの計算
		double sigma = Math.sqrt(1.0 / (double)(parentsSize - 1));

		//子個体の生成
		TPopulation children = new TPopulation();
		children.setPopulationSize(noOfOffspring);

		TVector temp = new TVector();

		//子個体の実数ベクトルの計算
		for(int i = 0; i < noOfOffspring; ++i) {
			//x_i = <y>
			children.getIndividual(i).getVector().copyFrom(yMean);

			for(int j = 0; j < parentsSize; ++j) {
				//nextGaussian() : 平均0.0, 標準偏差1.0 で乱数を取り出す
				double epsilon = sigma  * fRandom.nextGaussian();

				//y_iをセット
				temp.copyFrom(parents.getIndividual(j).getVector());

				//y_i - <y>
				temp.subtract(yMean);

				//ε(y_i - <y>)
				temp.scalarProduct(epsilon);

				//<y> + ε(y_i - <y>)
				children.getIndividual(i).getVector().add(temp);
			}
			//評価値セット
			double eval = ktablet.evaluate(children.getIndividual(i).getVector());
			children.getIndividual(i).setEvaluationValue(eval);
		}
		return children;
	}


	//テストコード
	public static void main(String[] args) {

		int seed = 0;
		int dim = 2;
		int noOfParents = 3;
		int noOfOffspring = 1000;
		TPopulation parents = new TPopulation();
		TRex rex = new TRex(new Random(seed));
		TKTablet tk = new TKTablet(1, dim);
		//3つの親をランダムに生成 2次元ベクトル空間
		parents.setPopulationSize(noOfParents);
		for(int i = 0;i < noOfParents; ++i) {
			parents.getIndividual(i).getVector().setDimension(dim);
			for(int j = 0;j < dim; ++j) {

				double x = rex.fRandom.nextGaussian();
				parents.getIndividual(i).getVector().setElement(j, x);
			}
			double eval = tk.evaluate(parents.getIndividual(i).getVector());
			parents.getIndividual(i).setEvaluationValue(eval);
		}


		TPopulation offspring = new TPopulation();
		offspring.setPopulationSize(noOfOffspring);
		offspring = rex.makeOffspring(parents, noOfOffspring);
		//System.out.println("FInish!");
		System.out.println(offspring);

		//ファイル書き込み
		String filename = ".\\childrenData.txt";
		String filename1 = ".\\ParentsData.txt";
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			offspring.writeTo(pw);
			pw.close();

			PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(filename1)));
			parents.writeTo(pw1);
			pw1.close();

		}catch(FileNotFoundException e) {
			System.out.println(e + "が見つかりません");
		}catch(IOException e) {
			System.out.println(e);
		}
	}
}
