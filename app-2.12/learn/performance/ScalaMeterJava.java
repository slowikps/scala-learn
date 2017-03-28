//package learn.performance;
//
//import org.scalameter.Context;
//import org.scalameter.japi.*;
//import org.scalameter.japi.annotation.*;
//import scala.Tuple2;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.Random;
//
//@scopes({
//        @scopeCtx(scope = "list", context = "config")
//})
//public class ScalaMeterJava extends JBench.OfflineReport {
//
//    public final Context config = new ContextBuilder()
//            .put("exec.benchRuns", 20)
//            .put("exec.independentSamples", 1)
//            .put("exec.outliers.covMultiplier", 1.5)
//            .put("exec.outliers.suspectPercent", 40)
//            .build();
//
//    public final JGen<Integer> sizes = JGen.intValues("size", 500000);
//
//    public final JGen<Tuple2<Integer, LinkedList<Integer>>> lists =
//            sizes.zip(sizes.map(
//                    new Fun1<Integer, LinkedList<Integer>>() {
//                        public LinkedList<Integer> apply(Integer v) {
//                            return new LinkedList<Integer>();
//                        }
//                    }
//            ));
//
//    public void listSetup(Tuple2<Integer, LinkedList<Integer>> v) {
//        int size = v._1();
//        LinkedList<Integer> list = v._2();
//
//        Random random = new Random(size);
//        for (int i = 0; i < size; i++) {
//            list.add(random.nextInt(size));
//        }
//    }
//
//    @gen("lists")
//    @setup("listSetup")
//    @curve("groupBy")
//    @benchmark("list.ops")
//    public HashMap<Integer, LinkedList<Integer>> histogram(
//            Tuple2<Integer, LinkedList<Integer>> v
//    ) {
//        LinkedList<Integer> list = v._2();
//
//        HashMap<Integer, LinkedList<Integer>> hm =
//                new HashMap<Integer, LinkedList<Integer>>();
//        for (int i = 0; i < 10; i++) {
//            hm.put(i, new LinkedList<Integer>());
//        }
//        Iterator<Integer> it = list.iterator();
//        while (it.hasNext()) {
//            Integer element = it.next();
//            LinkedList<Integer> tmp = hm.get(element % 10);
//            tmp.add(element);
//            hm.put(element % 10, tmp);
//        }
//        return hm;
//    }
//}
