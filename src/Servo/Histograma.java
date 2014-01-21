package Servo;

/**
 * TODO mostrar resuldatos num grafico
 * A simple histogram class. The setData(float f) finds in which bin
 * the value falls for nBins between the given minimum and maximum
 * values. An integer array keeps track of the number of times the input
 * value fell into a particular bin.
 */
public class Histograma {

    int [] bins = null;
    int nBins;
    float xLow,xHigh;
    float delBin;

    int overFlows=0,underFlows=0;

    String dataString=null;


    /**
     *
     * @param nBins
     * @param xLow
     * @param xHigh
     * @param _width
     * @param _height
     */
    Histograma (int nBins, float xLow, float xHigh,int _width,int _height){

        this.nBins = nBins;
        this.xLow  = xLow;
        this.xHigh = xHigh;

        bins = new int[nBins];
        delBin = (xHigh-xLow)/(float)nBins;

        reset();
    }

    /**
     * Extra constructor to allow for double values
     *
     * @param nBins
     * @param xLow
     * @param xHigh
     * @param _width
     * @param _height
     */

    Histograma (int nBins, double xLow, double xHigh,int _width,int _height){
        this(nBins, (float) xLow, (float) xHigh, _width, _height);
    }

    //----------------------------------------------------------------
    void setData(double data){
        setData((float)data);
    }
    //----------------------------------------------------------------
    void setData(float data){

        if( data < xLow)
            underFlows++;
        else if ( data >= xHigh)
            overFlows++;
        else{
            int bin = (int)((data-xLow)/delBin);
            if(bin >= 0 && bin < nBins) bins[bin]++;
        }
    }

    //----------------------------------------------------------------
    // To display the histogram in a chart, we need to pass the data
    // as a string.
    public void graphIt(){
        dataString = "";
        for (int i=0; i<nBins; i++){
            dataString += bins[i] + " ";
        }
    }


    //----------------------------------------------------------------
    public void reset(){
        dataString = "";
        for (int i=0; i<nBins; i++){
            bins[i]=0;
            dataString = dataString + bins[i] + " ";
            //fixme adicionar grafico de estrelinhas??
        }
    }

}


