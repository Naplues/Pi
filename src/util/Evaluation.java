package util;

/**
 * 对结果进行评估
 * recall:召回率
 * MAP: 平均准确度均值
 * MRR: 首位倒数排均值
 */
public class Evaluation {

    /**
     * 召回率
     *
     * @param bucket
     * @param k
     * @return
     */
    public static double recall(Bucket bucket, int k) {
        double rc = 0;
        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < k && j < features[i].length; j++) {
                if (features[i][j].isInducing()) {
                    rc++;
                    break;
                }
            }
        }
        rc /= bucket.getRevisionNumber();
        return rc;
    }

    /**
     * MRR
     *
     * @param bucket
     * @return
     */
    public static double MRR(Bucket bucket) {
        double mrr = 0.0;
        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isInducing()) {
                    mrr += 1.0 / (j + 1);
                    break;
                }
            }
        }
        mrr /= features.length;
        return mrr;
    }

    /**
     * MAP
     *
     * @param bucket
     * @return
     */
    public static double MAP(Bucket bucket) {
        double map = 0.0;
        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            double ap = 0.0;
            int k = 0;
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isInducing()) {
                    ap += (double) (k + 1) / (j + 1);
                    k++;
                }
            }
            ap /= k;
            map += ap;
        }
        map /= features.length;
        return map;
    }

    public static void evaluation(Bucket[] buckets) {
        String filePath = "C:\\Users\\gzq\\Desktop\\ChangeLocator\\pi\\output.csv";
        String result = "Version,Recall@1,Recall@5,Recall@10,MRR,MAP\n";

        double r1 = 0.0, r5 = 0.0, r10 = 0.0, map = 0.0, mrr = 0.0;
        for (Bucket bucket : buckets) {
            r1 += Evaluation.recall(bucket, 1);
            r5 += Evaluation.recall(bucket, 5);
            r10 += Evaluation.recall(bucket, 10);
            mrr += Evaluation.MRR(bucket);
            map += Evaluation.MAP(bucket);
            result += "v" + bucket.getVersionName() + ",";
            result += Evaluation.recall(bucket, 1) + ",";
            result += Evaluation.recall(bucket, 5) + ",";
            result += Evaluation.recall(bucket, 10) + ",";
            result += Evaluation.MRR(bucket) + ",";
            result += Evaluation.MAP(bucket) + "\n";
        }
        result += "Average,";
        result += r1 / buckets.length + ",";
        result += r5 / buckets.length + ",";
        result += r10 / buckets.length + ",";
        result += mrr / buckets.length + ",";
        result += map / buckets.length + "\n";

        //写入文件
        //FileHandle.writeStringToFile(filePath, result);
        //输出到控制台
        System.out.print(r1 / buckets.length + ",");
        System.out.print(r5 / buckets.length + ",");
        System.out.print(r10 / buckets.length + ",");
        System.out.print(mrr / buckets.length + ",");
        System.out.println(map / buckets.length);
    }
}