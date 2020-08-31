package RexJgg;

import java.io.IOException;
import java.util.Random;

public class TJgg {
	private Random fRandom;		      //乱数生成器
	private TKTablet fFunction;		  //KTablet関数
	private TRex fRex;				  //子個体生成器
	private TPopulation fPopulation; //集団
	private int fNoOfOffspring;	  //子個体生成数

	//コンストラクタ
	public TJgg(TKTablet func, int noOfOffspring, Random random) {
		fFunction = func;
		fNoOfOffspring = noOfOffspring;
		fRandom = random;
		fRex = new TRex(fRandom);
		fPopulation = new TPopulation();
	}

	public TPopulation initializePopulation(int size, double max, double min) {
		//1.初期集団生成
		//size個の個体を生成
		fPopulation.setPopulationSize(size);

		for(int i = 0;i < size; ++i) {
			//ベクトル参照
			TVector v = new TVector();
			v = fPopulation.getIndividual(i).getVector();
			v.setDimension(fFunction.getDimension());
			//一様乱数生成から各個体のベクトルの格納
			for(int j = 0;j < v.getDimension(); ++j) {
				double rand = fRandom.nextDouble() * (max - min) + min;
				v.setElement(j, rand);
			}
			//評価値格納
			double eval = fFunction.evaluate(v);
			fPopulation.getIndividual(i).setEvaluationValue(eval);
		}

		return fPopulation;
	}

	//複製選択
	public void doOneGeneration() {
		//2.複製選択
		//選択する親個体
		TPopulation parents = new TPopulation();
		parents.setPopulationSize(fFunction.getDimension() + 1);

		//選ばれた親個体の番号
		int[] fNoOfSelectParents = new int[fPopulation.getPopulationSize()];

		//非復元抽出
		for(int i = 0;i < parents.getPopulationSize(); ++i) {
			int idx;
			while(true) {
				//一様ランダムにインデックスを選択
				idx = fRandom.nextInt(fPopulation.getPopulationSize());
				boolean copy = false;
				//idxがすでに得られたかどうか調べる
				for(int j = 0;j < i; ++j) {
					copy = (fNoOfSelectParents[j] == idx);
				}
				if(!copy) break;
			}
			//インデックス番号を記憶
			fNoOfSelectParents[i] = idx;
			//親に選択
			parents.getIndividual(i).copyFrom(fPopulation.getIndividual(idx));
		}

		//3.子個体の生成
		TPopulation children = fRex.makeOffspring(parents, fNoOfOffspring);


		//4.子個体の評価
		//子個体のベクトルを関数に入れて評価値を保存する
		for(int i = 0; i < children.getPopulationSize(); ++i) {
			TVector childrenVector = children.getIndividual(i).getVector();
			double evaluationValue = fFunction.evaluate(childrenVector);

			children.getIndividual(i).setEvaluationValue(evaluationValue);
		}

		//5.複製選択
		children.sortByEvaluationValue();
		for(int i = 0;i < parents.getPopulationSize(); ++i) {
			fPopulation.getIndividual(fNoOfSelectParents[i]).copyFrom(children.getIndividual(i));
		}
	}

	//集団の最良評価値を返す
	public double getBestEvaluation() {
		fPopulation.sortByEvaluationValue();
		return fPopulation.getIndividual(0).getEValuationValue();
	}
	//集団の最良個体を選ぶ
	public TIndividual getBestIntdividual() {
		fPopulation.sortByEvaluationValue();
		return fPopulation.getIndividual(0);
	}

	//Rex/Jggのメイン
	public static void main(String[] args) throws IOException{


		int seed = (int)System.currentTimeMillis();
		int dim = 20;		//ベクトルの次元数
		int k = 1;		//k-tablet関数のパラメータk
		int sizeOfPopulation = 7*dim;	//集団サイズ
		int noOfOffspring = 6*dim;		//子個体生成数

		double min = -5.0;			//初期領域[-5.0 5.0]
		double max = 5.0;
		double threshold = 1.0e-5;		//打ち切り最良評価値
		int maxNoOfEvals = 4 * dim * 10000;	//打ち切り評価数

		int generation = 0;			//世代数
		int evalNum = noOfOffspring * generation;		//評価回数
		double bestEvalValue = 0.0;	//最高評価値

		//try {

			//String filename = ".\\RexJggData.txt";
			//PrintWriter pw =new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			//インスタンス生成
			TJgg jgg = new TJgg(new TKTablet(k, dim), noOfOffspring, new Random(seed));


			jgg.initializePopulation(sizeOfPopulation, max, min);
			bestEvalValue = jgg.getBestEvaluation();
			System.out.println("evalNum: " + evalNum + " BestEval: " + bestEvalValue);

			//打ち切り条件まで繰り返す
			while(evalNum < maxNoOfEvals && bestEvalValue > threshold) {
				jgg.doOneGeneration();
				bestEvalValue = jgg.getBestEvaluation();

				generation++;
				evalNum = noOfOffspring*generation;

				if(evalNum % 1000 == 0) {
					System.out.println("evalNum: " + evalNum + " BestEval: " + bestEvalValue);

				}

			}
			seed++;

		//}catch(IOException e) {
		//	System.out.println(e);
		//}
		System.out.println("Finish!");


	}

}
