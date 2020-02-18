package s4.B161822; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;
import java.util.HashMap;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
        return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { 
        myTarget = target;
    }
    
    public void setSpace(byte []space) { 
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
    }

    public double estimation(){
        /*
         boolean [] partition = new boolean[myTarget.length+1];
	
         int np;
         np = 1<<(myTarget.length-1);
         //System.out.println("np="+np+" length="+myTarget.length);
	
         double value = Double.MAX_VALUE; // value = mininimum of each "value1".
         for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
            // binary representation of p forms partition.
            // for partition {"ab" "cde" "fg"}
            // a b c d e f g   : myTarget
            // T F T F F T F T : partition:
            partition[0] = true; // I know that this is not needed, but..
            for(int i=0; i<myTarget.length -1;i++) {
                partition[i+1] = (0 !=((1<<i) & p));
            }
            partition[myTarget.length] = true;

            // Compute Information Quantity for the partition, in "value1"
            // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example

            double value1 = (double) 0.0;
            int end = 0;;
            int start = end;
            while(start<myTarget.length) {
                // System.out.write(myTarget[end]);
                end++;;
                while(partition[end] == false) {
                    // System.out.write(myTarget[end]);
                    end++;
                }
                // System.out.print("("+start+","+end+")");
                myFrequencer.setTarget(subBytes(myTarget, start, end));
                value1 = value1 + iq(myFrequencer.frequency());
                start = end;
            }
            // System.out.println(" "+ value1);

            // Get the minimal value in "value"
            if(value1 < value) value = value1;
         }
         */
	//DPcode
        double value = Double.MAX_VALUE;
        double value1 = (double)0.0;
        HashMap<Byte[],Double> memo = new HashMap<>();

        for(int i=0; i<myTarget.length; i++){
            byte [] q = subBytes(myTarget,0,i+1);
            double culcResult1 = 0.0;
            double culcResult2 = 0.0;
            if(memo.containsKey(q)){
                culcResult1 = memo.get(q);
            }else{
                myFrequencer.setTarget(q);
                culcResult1 = iq(myFrequencer.frequency());
            }
            q = subBytes(myTarget,i+1,myTarget.length);
            if(memo.containsKey(q)){
                culcResult2 = memo.get(q);
            }else{
                myFrequencer.setTarget(q);
                culcResult2 = iq(myFrequencer.frequency());
            }
            value1 = culcResult1 + culcResult2;
            if(value1 < value) value = value1;
        }
     
        return value;
    }
    
    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        long starttime = System.currentTimeMillis();
        myObject = new InformationEstimator();
        myObject.setSpace("123123123123012312301201247398723jfweorgesrghesrgeortujweiotuo438u5o4witfjelsfjsleghwaoghaleghwaitawegjhnharghwargjahlrgnjaegnjagna".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        //System.out.println(">0 "+value);
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        //System.out.println(">01 "+value);
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        //System.out.println(">0123 "+value);
        myObject.setTarget("012301230123".getBytes());
        value = myObject.estimation();
        //System.out.println(">00gheigtera "+value);
        long endtime = System.currentTimeMillis();
        System.out.println("prosessing time ="+ (endtime-starttime));
    }
}
				  
			       

	
    
