def getSketchesAccessor( combinatorialAccuracy ):
   import jpype
   classpath = "/home/ossy/development/git-repositories/yahoo-sketches/sketches-core/target/sketches-core-0.11.2-SNAPSHOT-with-shaded-memory.jar"
   jpype.startJVM(jpype.getDefaultJVMPath(), "-Djava.class.path=%s" % classpath)
   com = jpype.JPackage("com")
   comYahoo = com.yahoo
   comYahooSketches = com.yahoo.sketches
   comYahooSketchesFrequencies = com.yahoo.sketches.frequencies
   comYahooSketchesFrequenciesStringItemsSketch = com.yahoo.sketches.frequencies.StringItemsSketch
   mySketch = comYahooSketchesFrequenciesStringItemsSketch( combinatorialAccuracy )
   return mySketch

def getCustomSketchesAccessor( maxHashLengthInLog, initialHashLengthInLog ):
   import jpype
   classpath = "/home/ossy/development/git-repositories/yahoo-sketches/sketches-core/target/sketches-core-0.11.2-SNAPSHOT-with-shaded-memory.jar"
   jpype.startJVM(jpype.getDefaultJVMPath(), "-Djava.class.path=%s" % classpath)
   com = jpype.JPackage("com")
   comYahoo = com.yahoo
   comYahooSketches = com.yahoo.sketches
   comYahooSketchesFrequencies = com.yahoo.sketches.frequencies
   comYahooSketchesFrequenciesStringItemsSketch = com.yahoo.sketches.frequencies.StringItemsSketch
   mySketch = comYahooSketchesFrequenciesStringItemsSketch( maxHashLengthInLog, initialHashLengthInLog )
   return mySketch


