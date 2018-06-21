package com.yahoo.sketches.frequencies;
import com.yahoo.sketches.frequencies.ItemsSketch;
import com.yahoo.sketches.frequencies.ItemsSketch.Row;
import com.yahoo.sketches.frequencies.ErrorType;

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
   
   public Row<String>[] getFrequentItems(final long threshold) {
     return sortItems(threshold > getMaximumError() ? threshold : getMaximumError(), m_ErrorType);
   }
   
   protected ErrorType m_ErrorType = ErrorType.NO_FALSE_POSITIVES;
}
