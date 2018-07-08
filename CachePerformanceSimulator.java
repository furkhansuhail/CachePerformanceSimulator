
package cacheperformancesimulator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

   public class CachePerformanceSimulator {


       public static void main(String[] args) throws IOException 
    {
         // Initializing the Variables //
        int offsetlength;
        int indexlength;
        int taglength;
        int cacheset;
        int cacheblocks;
        int sizeofcache;
        int blocklength;
        int cacheassociativity;
        
      // Requesting the User to input the required configuration and trace file //

        System.out.print("\nPlease enter the file name without .cfg and .trc extension\n");
        
        System.out.print("Please select the desired Configuration file(.CFG) : ");
        
        BufferedReader bufferread = new BufferedReader(new InputStreamReader(System.in));
        String configname = bufferread.readLine();
        
        System.out.print("Select the desired trace file(.TRC): ");
        String tracename = bufferread.readLine();
     
                
     // opening the configuration file and  trace file and reading the files //
     
     // opening configuration file and reading the file //
        FileInputStream configurationfile;
        FileInputStream tracefile;
        BufferedReader configurationreader;
        BufferedReader tracereader;
        
        {
            //as we are requesting user not to add the extension in the file name we are assigning.cfg here//
            configname = configname + ".cfg";
            configurationfile = new FileInputStream(configname);
            configurationreader = new BufferedReader(new InputStreamReader(configurationfile));
            
         // The Conifuration file is being read line by line //
            String line = configurationreader.readLine();
            
            sizeofcache=Integer.parseInt(line.replaceAll("[\\D]", "")); 
            
            line = configurationreader.readLine();
            
            blocklength=Integer.parseInt(line.replaceAll("[\\D]", ""));
            
            line = configurationreader.readLine();
            
            cacheassociativity=Integer.parseInt(line.replaceAll("[\\D]", ""));
            
            // This is to compute the parameters of the given CACHE.//
            cacheblocks=sizeofcache/blocklength;
            
            cacheset=sizeofcache/(blocklength*cacheassociativity);
            
            offsetlength=(int) (Math.log(blocklength) / Math.log(2));
            
            indexlength=(int) (Math.log(cacheset) / Math.log(2));
            
            taglength=32-offsetlength-indexlength;  
            
          //Printing the Parameters of the given CACHE //
            System.out.printf("\n\nFramework of given CACHE:");
            System.out.printf("\nSize of the given CACHE: %d Bytes", sizeofcache);
            System.out.printf("\nBlock Size of the given CACHE: %d Bytes", blocklength);
            System.out.printf("\nAssociativity of CACHE: %d cacheblocks", cacheassociativity);
            System.out.printf("\nOffset Length of the given CACHE: %d Bits", offsetlength);
            System.out.printf("\nIndex Length of the given CACHE: %d Bits", indexlength);
            System.out.printf("\nTag Length Of the CACHE in the given file: %d Bits", taglength);
            System.out.printf("\nNumber Of Total Sets in given CACHE: %d Sets", cacheset);
            
            
            // Reading the Tracefile //
            
            tracename = tracename + ".trc";
            tracefile = new FileInputStream(tracename);
            tracereader = new BufferedReader(new InputStreamReader(tracefile));
            
            // Initializing all the vital variables //
            int memoryaddress;
            int correspondingset;
            int numberofhits;
                numberofhits=0;
            int noofmisses;
                noofmisses=0;
            int totalrequests;
                totalrequests=0;
            int correspondingtag;
            
            int tag[]=new int[100000];
            int valid[]=new int[100000];
            int rank[]=new int[100000];
            
              line = tracereader.readLine();
            while(line != null){
                String[] firstportion = line.split(" ");
                String[] secondportion = firstportion[0].split("x");
                memoryaddress = Integer.parseInt(secondportion[1], 16);
                
                correspondingset=(memoryaddress/blocklength)%cacheset;
                correspondingtag=(memoryaddress)/(blocklength*cacheset);
                
                int maxquevalue=0;
                int lastvalueinqueue=correspondingset*cacheassociativity;
                int flaghit=0;
                int hitindex=0;
                for (int i=correspondingset*cacheassociativity;i<(correspondingset+1)*cacheassociativity;i++)
                {
                    if (rank[i]>maxquevalue){maxquevalue=rank[i];
                    lastvalueinqueue=i;
                    }
                    if (valid[i]==1 && tag[i]==correspondingtag) {flaghit=1;hitindex=i;
                    
                    }
                }
                if (flaghit==1)
                {
                numberofhits++;
                for (int i=correspondingset*cacheassociativity;
                        i<(correspondingset+1)*cacheassociativity;
                        i++)
                {
                        if (rank[i]<rank[hitindex])
                        {
                            rank[i]++;
                        }
                    }
                   rank[hitindex]=0;
                }
                else
                {
                noofmisses++;
              
                for (int i=correspondingset*cacheassociativity;
                i<(correspondingset+1)*cacheassociativity;i++)
                {
                    if (i==lastvalueinqueue){tag[i]=correspondingtag;valid[i]=1;
                    rank[i]=0;
                    }
                    else {rank[i]++;
                }
                }
            }
            totalrequests++;
            line = tracereader.readLine();
            }
           
            // Computing Hit Rate//
            
            double hitratio;
            hitratio=(double) numberofhits/ (double) totalrequests;
            
            // Changing hit rate to percentage form //
            
            float finalhitratio;
            finalhitratio=(float) hitratio*100;
            
            
            System.out.printf("\n\nOutcome of the CACHE after simulation of the given files");
            System.out.printf("\nTotal Memory Requests: %d", totalrequests);
            System.out.printf("\nTotal Hits: %d", numberofhits);
            System.out.printf("\nTotal Misses of the given cache: %d", noofmisses);
            System.out.printf("\nHit Ratio of the given cache: %f", hitratio);
            System.out.printf("\nFinal Hit ratio of the given cache is: %f", finalhitratio);
            System.out.printf("\nend of program");
            System.out.printf("\n:) \n");
            
        } 
    }
    }