package Servo;
import org.math.plot.*;
import org.math.plot.plots.Plot;

import javax.swing.*;

/**
 * TODO mostrar resuldatos num grafico
 * A simple histogram class. The setData(float f) finds in which bin
 * the value falls for nBins between the given minimum and maximum
 * values. An integer array keeps track of the number of times the input
 * value fell into a particular bin.
 */
public class Histograma {

    double [] bins = null;
    double nBins;
    float xLow,xHigh;
    float delBin;

    int overFlows=0,underFlows=0;

    String dataString=null;
    Plot2DPanel plot = null;


    /**
     *
     * @param nBins
     * @param xLow
     * @param xHigh
     */
    Histograma (double nBins, float xLow, float xHigh){

        this.nBins = nBins;
        this.xLow  = xLow;
        this.xHigh = xHigh;

        bins = new double[(int)nBins];
        delBin = (xHigh-xLow)/(float)nBins;

        reset();
        plot = new Plot2DPanel();
    }

    /**
     * Extra constructor to allow for double values
     *
     * @param nBins
     * @param xLow
     * @param xHigh
     */

    Histograma (double nBins, double xLow, double xHigh){
        this(nBins, (float) xLow, (float) xHigh);
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

        plot.addBarPlot("Histogram", bins);
        JFrame frame = new JFrame("Histograma");
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(400,300);
    }


    //----------------------------------------------------------------
    public void reset(){
        dataString = "";
        for (int i=0; i<nBins; i++){
            bins[i]=0;
            dataString = dataString + bins[i] + " ";
        }
    }

}


