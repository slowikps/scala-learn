Based on: 
* "Keep It Simple with Scala"
* http://docs.scala-lang.org/overviews/core/futures.html

0. Intro
    * I am going to use only real live examples 

1. Describe the problem:  
    * Beer is already warm - before we get pizza ; /
    * We need to send someone for pizza and beer at the same time
2. 

    * Wrap into Futures 
    * Unwrap them in the for comprehension
    * Main program will terminate before we finish makeing our treat: 
        * Await.result(fut, 10.seconds)
    * Talk about execution context
        * Did not help - as we are doing map/flat map 
        
        
Should I tell anything about: 
   * Await - block to unwrap value by macro in compile time? 