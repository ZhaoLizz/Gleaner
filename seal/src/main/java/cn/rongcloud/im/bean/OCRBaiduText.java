package cn.rongcloud.im.bean;

import java.util.List;

public class OCRBaiduText {


    /**
     * log_id : 4127351120582339804
     * words_result_num : 5
     * words_result : [{"words":"中易园沿与置","probability":{"variance":0.019552,"average":0.607888,"min":0.407817}},{"words":"影H斗针器","probability":{"variance":0.040508,"average":0.694329,"min":0.359224}},{"words":"案3击群￥:岁","probability":{"variance":0.051845,"average":0.74935,"min":0.355299}},{"words":"9vv60∠09↓:舌击","probability":{"variance":0.058157,"average":0.784866,"min":0.42637}},{"words":"军解:2","probability":{"variance":0.066234,"average":0.706525,"min":0.379959}}]
     */

    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : 中易园沿与置
         * probability : {"variance":0.019552,"average":0.607888,"min":0.407817}
         */

        private String words;
        private ProbabilityBean probability;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public ProbabilityBean getProbability() {
            return probability;
        }

        public void setProbability(ProbabilityBean probability) {
            this.probability = probability;
        }

        public static class ProbabilityBean {
            /**
             * variance : 0.019552
             * average : 0.607888
             * min : 0.407817
             */

            private double variance;
            private double average;
            private double min;

            public double getVariance() {
                return variance;
            }

            public void setVariance(double variance) {
                this.variance = variance;
            }

            public double getAverage() {
                return average;
            }

            public void setAverage(double average) {
                this.average = average;
            }

            public double getMin() {
                return min;
            }

            public void setMin(double min) {
                this.min = min;
            }
        }
    }
}
