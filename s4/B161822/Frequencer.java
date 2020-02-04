package s4.B161822;
import java.lang.*;
import s4.specification.*;


/*package s4.specification;
 ここは、１回、２回と変更のない外部仕様である。
 public interface FrequencerInterface {     // This interface provides the design for frequency counter.
 void setTarget(byte  target[]); // set the data to search.
 void setSpace(byte  space[]);  // set the data to be searched target from.
 int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
 //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
 //Otherwise, get the frequency of TAGET in SPACE
 int subByteFrequency(int start, int end);
 // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
 // For the incorrect value of START or END, the behavior is undefined.
 }
 */



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    
    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。
    
    
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.
    
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.
    
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }
    
    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.
        // Example of dictionary order
        // "i"      <  "o"        : compare by code
        // "Hi"     <  "Ho"       ; if head is same, compare the next element
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
        //
        //The return value of "int suffixCompare" is as follows.
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;
        
        // ここにコードを記述せよ
        
        
        if(i == j) return 0;
        int k = 0;
        
        while(true){
            
            if(mySpace[i + k] < mySpace[j + k]){
                return -1;
            }
            else if(mySpace[j + k] < mySpace[i + k]){
                return 1;
            }
            else if(i + k == mySpace.length - 1){
                return -1;
            }
            else if(j + k == mySpace.length - 1){
                return 1;
            }
            k++;
            
        }
        //
        //return 0; // この行は変更しなければいけない。
    }
    
    public void setSpace(byte []space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //
        // ここに、int suffixArrayをソートするコードを書け。
	/* バブルソート
        for(int i = 0; i < space.length - 1; i++){
            for(int j = 0; j <  space.length - i - 1; j++){
                if(suffixCompare(suffixArray[j],suffixArray[j+1])== 1){
                    int temp = suffixArray[j];
                    suffixArray[j] = suffixArray[j+1];
                    suffixArray[j+1] = temp;
                }
            }
        }
	*/
	q_sort(0, suffixArray.length-1);
        // 　順番はsuffixCompareで定義されるものとする。
    }

    private void q_sort(int x, int y) {
	if(x < y){
	    int pos = partition(x,y);
	    q_sort(x,pos-1);
	    q_sort(pos+1,y);
	}
    }

    private int partition(int x, int y) {
	int l = x;
	int r = y-1;

	while(true){
	    while(true){
		if(suffixCompare(suffixArray[l],suffixArray[y])==-1){
		    l++;
		}else{
		    break;
		}
	    }
	    while(true){
		if(suffixCompare(suffixArray[r],suffixArray[y])==1){
		    r--;
		}else{
		    break;
		}
	    }
	    if(l>=r){
		break;
	    }
	    int temp = suffixArray[l];
	    suffixArray[l] = suffixArray[r];
	    suffixArray[r] = temp;
	}
	int temp = suffixArray[l];
	suffixArray[l] = suffixArray[y];
	suffixArray[y] = temp;
	return l;
    }
    
    // Suffix Arrayを用いて、文字列の頻度を求めるコード
    // ここから、指定する範囲のコードは変更してはならない。
    
    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }
    
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    
    
    public int subByteFrequency(int start, int end) {
        /* This method be work as follows, but much more efficient
         int spaceLength = mySpace.length;
         int count = 0;
         for(int offset = 0; offset< spaceLength - (end - start); offset++) {
         boolean abort = false;
         for(int i = 0; i< (end - start); i++) {
         if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
         }
         if(abort == false) { count++; }
         }
         */
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。
    
    private int targetCompare(int i, int j, int k) {
        // suffixArrayを探索するときに使う比較関数。
        // 次のように定義せよ
        // suffix_i is a string in mySpace starting at i-th position.
        // target_i_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_i_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_i_k it return 1;
        // if suffix_i < target_i_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // ここに比較のコードを書け
        //
        for(int a = 0; a < k-j; a++){
	    if((suffixArray[i]+a)>=mySpace.length){
		return -1;
	    }
            if(mySpace[suffixArray[i]+a]>myTarget[j+a]){
                return 1;
            }else if(mySpace[suffixArray[i]+a]<myTarget[j+a]){
                return -1;
            }
        }
        return 0; // この行は変更しなければならない。
    }

    private int b_search(int x, int y, int start, int end) {
	int p = x+(y-x)/2;
	int result = targetCompare(p,start,end);
	System.out.println(result+":"+p);

	if(result==0){
	    return p;
	}else if(result==-1){
	    if(x>=y){
	        return -1;
	    }
	    return b_search(p+1, y, start, end);	    
	}
	else{
	    if(x>=y){
	        return -1;
	    }
	    return b_search(x, p-1, start, end);
	}
    }
    
    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        
        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.
        //
        // ここにコードを記述せよ。
        //
        
        /*
        for(int i = 0; i < suffixArray.length; i++){
            if(targetCompare(i,start,end) >= 0){
		//System.out.println("start index="+i);
		return i;
	    }
        }
	*/
	//二分探索
	int index = b_search(0,suffixArray.length-1,start,end);
	System.out.println("index ="+index);
	if(index==-1){
	    return index;
	}
	while(true){
	    if(index<0){
		index=0;
		break;
	    }
	    if(targetCompare(index,start,end)==0){
		index--;
	    }else{
		index++;
		break;
	    }
	}
	System.out.println("start index="+index);
	return index;
    }
    
    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
        //
        //　ここにコードを記述せよ
        //
        /*
        for(int i = 0; i < suffixArray.length; i++){
            if(targetCompare(i,start,end) == 1){
		//System.out.println("end index="+i);
		return i;
	    }
        }
	*/
	//二分探索
	int index = b_search(0,suffixArray.length-1,start,end);
	System.out.println("index ="+index);
	if(index==-1){
	    System.out.println("end index="+index);
	    return index;
	}
	while(true){
	    if(index>=suffixArray.length){
		index=suffixArray.length;
		break;
	    }
	    if(targetCompare(index,start,end)==0){
		index++;
	    }else{
		break;
	    }
	}
	System.out.println("end index="+index);
	return index;
        // この行は変更しなければならない、
    }
    
    
    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray(); // you may use this line for DEBUG
            /* Example from "Hi Ho Hi Ho"
             0: Hi Ho
             1: Ho
             2: Ho Hi Ho
             3:Hi Ho
             4:Hi Ho Hi Ho
             5:Ho
             6:Ho Hi Ho
             7:i Ho
             8:i Ho Hi Ho
             9:o
             A:o Hi Ho
             */
            
            frequencerObject.setTarget("pH".getBytes());
            //
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //
            
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(0 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
            
            
            int start = frequencerObject.subByteStartIndex(0,1);
            int end = frequencerObject.subByteEndIndex(0,1);
            int result2 = end - start;
            System.out.print("subFreq = " + result2+ " ");
            if(0 == result2) {System.out.println("OK");} else {System.out.println("WRONG");}
            
            
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

