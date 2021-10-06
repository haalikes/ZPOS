package com.watata.zpos;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataHelper {

    public DataHelper() {
    }

    public String removeDoubleQuoteInCsv(String s){
        boolean startQuote = false;
        String returnS= "";
        String sSingle;

        if (s.indexOf("\"") > 0){
            for(int i=0; i < s.length(); i++) {
                sSingle = s.substring(i,i+1);

                if(sSingle.equals("\"")){
                    if (startQuote){
                        startQuote = false;
                    } else {
                        startQuote = true;
                    }
                }

                if (startQuote){
                    if (sSingle.equals(",")){
                        sSingle = ";";
                    }
                }
                if (!sSingle.equals("\"")){
                    returnS = returnS + sSingle;
                }

            }

            Log.d("texto=", "returnS=" + returnS);

            return returnS;
        } else {
            return s;
        }


    }

    public String getSubString(String s, String separator, int occurance){
        String newS = s;

        if (occurance!=0){
            for (int i = 0; i < occurance; i++) {
                newS = newS.substring(newS.indexOf(separator)+1);
            }
        }

        newS = newS.substring(0, newS.indexOf(separator));

        return newS;
    }

    public String getDateMonDDYYYY(String dd, String mm, String yyyy){
        String mon;
        int mmInt = Integer.parseInt(mm);

        switch(mmInt){
            case 0:
                mon = "Jan";
                break;
            case 1:
                mon = "Feb";
                break;
            case 2:
                mon = "Mar";
                break;
            case 3:
                mon = "Apr";
                break;
            case 4:
                mon = "May";
                break;
            case 5:
                mon = "Jun";
                break;
            case 6:
                mon = "Jul";
                break;
            case 7:
                mon = "Aug";
                break;
            case 8:
                mon = "Sep";
                break;
            case 9:
                mon = "Oct";
                break;
            case 10:
                mon = "Nov";
                break;
            case 11:
                mon = "Dec";
                break;
            default:
                mon = "Jan";
                break;
        }

        return mon + " " + dd + ", " + yyyy;

    }

    public List<String> listStringFrSemi(String s){
        String newS = s;
        List<String> listMod = new LinkedList<>();
        listMod.clear();

        if (s.indexOf(";") > 0){
            newS = newS + ";";
            for(int i=0; i<countChar(s,";");i++){
                listMod.add( newS.substring(0,newS.indexOf(";")));
                newS = newS.substring(newS.indexOf(";"));
            }
        } else {
            listMod.add(s);
        }

        return listMod;
    }

    public int countChar(String s, String c)
    {
        String sNew = s;
        int count = 0;

        for(int i=0; i < s.length(); i++) {
            if(sNew.substring(0,1).equals(c)){
                count++;
            }
            sNew = sNew.substring(1);
        }
        return count;
    }

    public List<String> listSortUnique(List<String> listString){
        List<String> listReturn = new LinkedList<>();
        listReturn.clear();

        for (int i=0; i < listString.size(); i++){
            if (!listReturn.contains(listString.get(i))){
                listReturn.add(listString.get(i));
            }
        }

        Collections.sort(listReturn);

        return listReturn;
    }

    public void popMessage(String s, Context c){
        Toast.makeText(c, "" + s, Toast.LENGTH_LONG).show();
    }


}
