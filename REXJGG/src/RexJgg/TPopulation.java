package RexJgg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TPopulation {
	private TIndividual[] fArray;

	public TPopulation() {
		fArray = new TIndividual[0];
	}
	public TPopulation(TPopulation src) {
		fArray = new TIndividual[src.fArray.length];
		for(int i = 0;i < fArray.length; ++i) {
			fArray[i] = new TIndividual(src.fArray[i]);
		}
	}

	@Override
	public TPopulation clone() {
		return new TPopulation(this);
	}

	public TPopulation copyFrom(TPopulation src) {
		setPopulationSize(src.getPopulationSize());
		for(int i = 0;i < fArray.length; ++i) {
			fArray[i].copyFrom(src.fArray[i]);
		}
		return this;
	}
	@Override
	public String toString() {
		String str= "Population Size:" + fArray.length + "\n";
		for(int i = 0;i < fArray.length; ++i) {
			str += fArray[i].toString();
			str += "\n";
		}
		return str;
	}

	public void setPopulationSize(int size) {
		if(fArray.length != size) {
			fArray  = new TIndividual[size];
			for(int i = 0; i < size; ++i) {
				fArray[i] = new TIndividual();
			}
		}
	}
	public int getPopulationSize() {
		return fArray.length;
	}

	public void writeTo(PrintWriter pw) {
		pw.println(fArray.length);
		for(int i = 0;i < fArray.length; ++i) {
			fArray[i].writeTo(pw);
		}
	}
	public void readFrom(BufferedReader br) throws IOException{
		int size = Integer.parseInt(br.readLine());
		setPopulationSize(size);
		for(int i = 0;i < fArray.length; ++i) {
			fArray[i].readFrom(br);
		}
	}

	public TIndividual getIndividual(int index) {
		return fArray[index];
	}

	public void swap(int index1, int index2) {
		TIndividual tmp = fArray[index1];
		fArray[index1] = fArray[index2];
		fArray[index2] = tmp;
	}

	public void sortByEvaluationValue() {
		//バブルソート
		for(int i = 0; i < fArray.length - 1; ++i) {
			for(int j = i + 1; j < fArray.length; ++j) {
				if(fArray[i].getEValuationValue() > fArray[j].getEValuationValue()) {
					TIndividual tmp = fArray[i];
					fArray[i] = fArray[j];
					fArray[j] = tmp;
				}
			}
		}
	}

	//テストコード
	public static void main(String[] args) {
		TPopulation src = new TPopulation();
		//初期状態src確認
		System.out.println("Test1:コンストラクタ");
		System.out.println(src.fArray.length);
		System.out.println(src);

		//コピーコンストラクタ
		System.out.println("\n" + "Test2:コピーコンストラクタ");
		//集団サイズ2
		src.setPopulationSize(2);
		for(int i = 0; i < src.getPopulationSize(); ++i) {
			//各個体に評価値設定
			src.getIndividual(i).setEvaluationValue(i + 1);
			//各個体の実数値配列に次元数設定
			src.getIndividual(i).getVector().setDimension(5);
			//各個体の実数値配列に要素設定
			for(int j = 0;j < src.getIndividual(i).getVector().getDimension(); ++j) {
				src.getIndividual(i).getVector().setElement(j, 5 * (j + i));
			}
			System.out.println("src[" + i + "] :" +src.getIndividual(i).getVector());
		}
		TPopulation p1 = new TPopulation(src);
		System.out.println(p1.getPopulationSize());
		for(int i = 0; i < p1.getPopulationSize(); ++i) {
			System.out.println(i + " : " + p1.getIndividual(i).getVector().equals(src.getIndividual(i).getVector()));
		}

		//クローン
		System.out.println("\n" + "Test3:クローン生成");
		TPopulation pop2 = new TPopulation();
		pop2 = p1.clone();
		System.out.println(pop2.getPopulationSize());
		for(int i = 0; i < pop2.getPopulationSize(); ++i) {
			System.out.println(p1.getIndividual(i).getVector().equals(pop2.getIndividual(i).getVector()));
		}

		//copyfrom
		System.out.println("\n" + "Test4:" +"copyFrom");
		TPopulation p_1 = new TPopulation();
		TPopulation p_2 = new TPopulation();

		p_1.setPopulationSize(5);
		for(int i = 0; i < p_1.getPopulationSize(); ++i) {
			p_1.getIndividual(i).setEvaluationValue(i);
			p_1.getIndividual(i).getVector().setDimension(2);
			for(int j = 0; j < p_1.getIndividual(i).getVector().getDimension(); ++j) {
				p_1.getIndividual(i).getVector().setElement(j, j+1);
			}
		}
		p_2.copyFrom(p_1);
		System.out.println(p_2);


		//toString
		System.out.println("\n" + "Test5:ToString" );
		TPopulation popToStr = new TPopulation();

		popToStr.setPopulationSize(2);
		for(int i = 0; i < popToStr.getPopulationSize(); ++i) {
			popToStr.getIndividual(i).setEvaluationValue((double)i);
			popToStr.getIndividual(i).getVector().setDimension(3);
			for(int j = 0; j < popToStr.getIndividual(i).getVector().getDimension(); ++j) {
				popToStr.getIndividual(i).getVector().setElement(j, (double)j);
			}
		}
		System.out.println(popToStr);

		//WtiteTo
		System.out.println("\nTest6:WriteTo");
		TPopulation pWriteTo = new TPopulation();
		pWriteTo.copyFrom(p_2);

		String filename = ".\\TPopulationTestWriteTo.txt";
		try {
			//ファイル書き込み
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			pWriteTo.writeTo(pw);
			pw.close();
			//書き込み終了

			//ファイル読み込みのためのBufferedReaderのインスタンス生成
			BufferedReader br = new BufferedReader(new FileReader(filename));

			//個体数表示  2
			System.out.println(Integer.parseInt(br.readLine()));
			for(int i = 0;i < pWriteTo.getPopulationSize(); ++i) {
				System.out.println(i+1 + "番目");
				//評価値
				System.out.println("評価値: " + Double.parseDouble(br.readLine()));
				//ベクトルの次元
				System.out.println("次元数: " + Integer.parseInt(br.readLine()));
				//i番目の個体のベクトルの値
				System.out.println("ベクトル値: " + br.readLine());
			}
			br.close();
		}catch(FileNotFoundException e) {
			System.out.println(filename + "が見つかりません");
		}catch(IOException e) {
			System.out.println(e);
		}

		//ReadFrom
		System.out.println("\nTest7:ReadFrom");
		TPopulation pReadFrom = new TPopulation();
		pReadFrom.copyFrom(pWriteTo);
		String filename1 = ".\\TPopulationTestReadFrom.txt";
		try {
			//ファイル書き込み
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename1)));
			pReadFrom.writeTo(pw);
			pw.close();
			//書き込み終了

			TPopulation p2 = new TPopulation();

			//ファイル読み込み
			BufferedReader br = new BufferedReader(new FileReader(filename1));
			p2.readFrom(br);
			br.close();

			System.out.println("Population Size:" + p2.getPopulationSize());
			for(int i = 0; i < p2.getPopulationSize(); ++i) {
				System.out.println(i + 1 + "番目");
				System.out.println("評価値 :" + p2.getIndividual(i).getEValuationValue());
				System.out.println("ベクトル次元 :" + p2.getIndividual(i).getVector().getDimension());
				System.out.println("ベクトル値 :" + p2.getIndividual(i).getVector());
			}

		}catch(FileNotFoundException e) {
			System.out.println(filename + "が見つかりません");
		}catch(IOException e) {
			System.out.println(e);
		}

		//ソート
		System.out.println("\nTest8:Sort");
		TPopulation pSort = new TPopulation();
		pSort.setPopulationSize(5);
		System.out.println("ソート前");
		for(int i = 0;i < pSort.getPopulationSize(); ++i) {
			pSort.getIndividual(i).setEvaluationValue((double)5 - i);
			System.out.println(pSort.getIndividual(i).getEValuationValue());
		}
		pSort.sortByEvaluationValue();
		System.out.println("ソート後");
		for(int i = 0;i < pSort.getPopulationSize(); ++i) {
			System.out.println(pSort.getIndividual(i).getEValuationValue());
		}


	}

}
