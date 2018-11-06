package spamdetection;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
      Bayes bs=new Bayes();
      try {
		bs.countHamAndSpam("H:\\workspace\\SpamDetect\\src\\spamdetection\\train.txt");
		//bs.createTestfile();
		//bs.readTestFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
