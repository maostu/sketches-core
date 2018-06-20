package com.yahoo.sketches.frequencies;
import com.yahoo.sketches.frequencies.ItemsSketch;

public class StringItemsSketch extends com.yahoo.sketches.frequencies.ItemsSketch<String>
{
   public StringItemsSketch( int maxMapSize )
   {
      super(maxMapSize);
   }
   
   public StringItemsSketch( final int lgMaxMapSize, final int lgCurMapSize )
   {
      super(lgMaxMapSize, lgCurMapSize);
   }
}
