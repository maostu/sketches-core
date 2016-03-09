/*
 * Copyright 2015, Yahoo! Inc.
 * Licensed under the terms of the Apache License 2.0. See LICENSE file at the project root for terms.
 */
package com.yahoo.sketches.theta;

import static com.yahoo.sketches.theta.PreambleUtil.COMPACT_FLAG_MASK;
import static com.yahoo.sketches.theta.PreambleUtil.EMPTY_FLAG_MASK;
import static com.yahoo.sketches.theta.PreambleUtil.ORDERED_FLAG_MASK;
import static com.yahoo.sketches.theta.PreambleUtil.READ_ONLY_FLAG_MASK;
import static com.yahoo.sketches.theta.PreambleUtil.RETAINED_ENTRIES_INT;
import static com.yahoo.sketches.theta.PreambleUtil.THETA_LONG;
import static com.yahoo.sketches.theta.PreambleUtil.extractFlags;
import static com.yahoo.sketches.theta.PreambleUtil.extractPreLongs;
import static com.yahoo.sketches.theta.PreambleUtil.extractSeedHash;

import com.yahoo.sketches.memory.Memory;

/**
 * An off-heap (Direct), compact, ordered, read-only sketch. This sketch may be associated
 * with Serial Versions 1, 2, or 3.
 * 
 * @author Lee Rhodes
 */
class DirectCompactOrderedSketch extends CompactSketch {
  private Memory mem_;
  private int preLongs_; //1, 2, or 3.
  
  private DirectCompactOrderedSketch(boolean empty, short seedHash, int curCount, long thetaLong) {
    super(empty, seedHash, curCount, thetaLong);
  }
  
  /**
   * Wraps the given Memory, which may be a SerVer 1, 2, or 3 sketch.
   * @param srcMem <a href="{@docRoot}/resources/dictionary.html#mem">See Memory</a>
   * @param pre0 the first 8 bytes of the preamble
   * @return this sketch
   */
  static DirectCompactOrderedSketch wrapInstance(Memory srcMem, long pre0) {
    int preLongs = extractPreLongs(pre0);
    int flags = extractFlags(pre0);
    boolean empty = (flags & EMPTY_FLAG_MASK) > 0;
    short seedHash = (short) extractSeedHash(pre0);
    int curCount = (preLongs > 1) ? srcMem.getInt(RETAINED_ENTRIES_INT) : 0;
    long thetaLong = (preLongs > 2) ? srcMem.getLong(THETA_LONG) : Long.MAX_VALUE;
    DirectCompactOrderedSketch dcos = new DirectCompactOrderedSketch(empty, seedHash, curCount, thetaLong);
    dcos.preLongs_ = preLongs;
    dcos.mem_ = srcMem;
    return dcos;
  }
  
  /**   //TODO convert to factory
   * Converts the given UpdateSketch to this compact ordered form.
   * @param sketch the given UpdateSketch
   * @param dstMem the given destination Memory. This clears it before use.
   */
  DirectCompactOrderedSketch(UpdateSketch sketch, Memory dstMem) {
    super(sketch.isEmpty(), 
        sketch.getSeedHash(), 
        sketch.getRetainedEntries(true), //curCount_  set here 
        sketch.getThetaLong()            //thetaLong_ set here
        );
    int emptyBit = isEmpty()? (byte) EMPTY_FLAG_MASK : 0;
    byte flags = (byte) (emptyBit |  READ_ONLY_FLAG_MASK | COMPACT_FLAG_MASK | ORDERED_FLAG_MASK);
    boolean ordered = true;
    long[] compactOrderedCache = 
        CompactSketch.compactCache(sketch.getCache(), getRetainedEntries(false), getThetaLong(), ordered);
    
    mem_ = loadCompactMemory(compactOrderedCache, isEmpty(), getSeedHash(), 
        getRetainedEntries(false), getThetaLong(), dstMem, flags);
  }
  
  
  /**  //TODO convert to factory
   * Constructs this sketch from correct, valid components.
   * @param compactOrderedCache in compact, ordered form
   * @param empty The correct <a href="{@docRoot}/resources/dictionary.html#empty">Empty</a>.
   * @param seedHash The correct <a href="{@docRoot}/resources/dictionary.html#seedHash">Seed Hash</a>.
   * @param curCount correct value
   * @param thetaLong The correct <a href="{@docRoot}/resources/dictionary.html#thetaLong">thetaLong</a>.
   * @param dstMem the destination Memory.  This clears it before use.
   */
  DirectCompactOrderedSketch(long[] compactOrderedCache, boolean empty, short seedHash, int curCount, 
      long thetaLong, Memory dstMem) {
    super(empty, seedHash, curCount, thetaLong);
    int emptyBit = isEmpty()? (byte) EMPTY_FLAG_MASK : 0;
    byte flags = (byte) (emptyBit |  READ_ONLY_FLAG_MASK | COMPACT_FLAG_MASK | ORDERED_FLAG_MASK);
    mem_ = loadCompactMemory(compactOrderedCache, empty, seedHash, curCount, thetaLong, dstMem, flags);
  }
  
  //Sketch interface
  
  @Override
  public byte[] toByteArray() {
    return DirectCompactSketch.compactMemoryToByteArray(mem_, getRetainedEntries(false));
  }

  //restricted methods
  
  @Override
  public boolean isDirect() {
    return true; 
  }
  
  //SetArgument "interface"
  
  @Override
  long[] getCache() {
    long[] cache = new long[getRetainedEntries(false)];
    mem_.getLongArray(preLongs_ << 3, cache, 0, getRetainedEntries(false));
    return cache;
  }
  
  @Override
  Memory getMemory() {
    return mem_;
  }

  @Override
  public boolean isOrdered() {
    return true;
  }
  
}
