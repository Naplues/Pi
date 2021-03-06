package nju.gzq.selector.fc;

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
    public static double recall(Project bucket, int k, int[] array) {
        double rc = .0;

        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < k && j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    rc++;
                    array[i] = 1;
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
    public static double MRR(Project bucket) {
        double mrr = .0;
        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    mrr += 1.0 / (j + 1);
                    break;
                }
            }
        }
        mrr /= bucket.getRevisionNumber();
        return mrr;
    }

    /**
     * MAP
     *
     * @param bucket
     * @return
     */
    public static double MAP(Project bucket) {
        double map = 0.0;
        Feature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            double ap = 0.0;
            int k = 0;
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    ap += (double) (k + 1) / (j + 1);
                    k++;
                }
            }
            ap /= k;
            map += ap;
        }
        map /= bucket.getRevisionNumber();
        return map;
    }

    /**
     * 评估单个项目的性能
     *
     * @param bucket
     * @return
     */
    public static double[] evaluation(Project bucket, boolean details) {
        double[] result = new double[5];
        int[] array = new int[bucket.getFilterNumber()];
        result[0] = Evaluation.recall(bucket, 1, array);
        result[1] = Evaluation.recall(bucket, 5, array);
        result[2] = Evaluation.recall(bucket, 10, array);
        result[3] = Evaluation.MRR(bucket);
        result[4] = Evaluation.MAP(bucket);
        if (details) {
            for (int i = 0; i < result.length; i++) System.out.print(result[i] + ", ");
            System.out.println(bucket.getFilterNumber() + "/" + bucket.getRevisionNumber());
        }
        return result;
    }

    /**
     * 评估多个项目的平均性能
     *
     * @param buckets
     * @param details
     * @return
     */
    public static double[] evaluation(Project[] buckets, boolean details) {

        double r1 = 0.0, r5 = 0.0, r10 = 0.0, map = 0.0, mrr = 0.0;
        for (Project bucket : buckets) {
            double[] result = evaluation(bucket, details);
            r1 += result[0];
            r5 += result[1];
            r10 += result[2];
            mrr += result[3];
            map += result[4];
        }

        //输出到控制台
        double[] values = new double[5];
        values[0] = r1 / buckets.length;
        values[1] = r5 / buckets.length;
        values[2] = r10 / buckets.length;
        values[3] = mrr / buckets.length;
        values[4] = map / buckets.length;

        for (int i = 0; i < values.length; i++) {
            // System.out.print(values[i] + ",");
        }
        // System.out.println();
        return values;
    }

}
