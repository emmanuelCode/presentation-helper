package com.pitonneux.presentation_helper.GridViewPager;

import android.app.Fragment;
import android.content.Context;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.view.Gravity;

/**
 * Created by cacau on 1/17/2017.
 */

public class SimpleGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
   // private List mRows;
    private String[]data;

    public SimpleGridPagerAdapter(Context ctx,String[] data ,FragmentManager fm){
        super(fm);
        mContext= ctx;
        this.data=data;
    }

    //simple container for static data in each page
    //add int attributes to fetch resources to a class name Page
   /* private static class Page{
        //static resources
        int titleRes;
        int textRes;
        int cardGravity;
        // add more here
        boolean expansionEnabled;
        int expansionDirection;
        boolean expansionFactor;
    }*/


  /*  public static Page[][] setPages(Page pages,int r,int c){
        Page[][] pageTemp= new Page[r][c];

        for(int rows=0;rows<pageTemp.length;rows++){
            for(int columns=0;columns<pageTemp[rows].length;columns++){
                pageTemp[rows][columns].titleRes=
            }
        }

      return null;
    }
    // Create a static set of pages in a 2D array
    private final Page[][] PAGES=
            {
            {}
    };//?*/



    @Override
    public Fragment getFragment(int row, int col) {
        int slideCount=col+1;
        /*Page page= PAGES[row][col];//ok

        String title = page.titleRes != 0 ? mContext.getString(page.titleRes) : null;//to check out
        String text = page.textRes != 0 ? mContext.getString(page.textRes) : null;// to check out*/

        CardFragment cardFragment=CardFragment.create("slide:"+slideCount+"/"+data.length,data[col]);

        //Fragment fragment= new Fragment();



       // cardFragment.getView().setLayoutParams(?);



        //TODO: set card layout to fullScreen tried everything don t know how
        cardFragment.setCardGravity(Gravity.TOP);
        cardFragment.setExpansionEnabled(true);
        cardFragment.setExpansionDirection(CardFragment.EXPAND_DOWN);


        return cardFragment;

    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return data.length;
    }
}
