package com.jaehee.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 걷기 검사 후의 값을 입력받아 데이터 가공을 진행하는 클래스입니다.
 */
public class FootStepPressureProcessor {
    private ArrayList<ArrayList<Integer>> leftPressure;
    private ArrayList<ArrayList<Integer>> rightPressure;
    private int step;
    private int len_standard;
    private ArrayList<Integer> leftPressureSum;
    private ArrayList<Integer> rightPressureSum;

    public FootStepPressureProcessor(){
        step = 0;
        len_standard = 10;
        leftPressure = new ArrayList<ArrayList<Integer>>();
        rightPressure = new ArrayList<ArrayList<Integer>>();
        leftPressureSum = new ArrayList<Integer>();
        rightPressureSum = new ArrayList<Integer>();
    }

    /**
     * FootData 배열을 입력받아 압력 합이 0이 되는 곳을 기준으로 한 걸음 단위로 나눔
     * @param d  걷기인 경우, 센서로부터 입력받은 값의 배열
     */
    public void process(FootData[] d){
        Arrays.sort(d);
        int [][]p;
        ArrayList<Integer> tmp_l = new ArrayList<Integer>();
        ArrayList<Integer> tmp_r = new ArrayList<Integer>();
        boolean l_zero = true, r_zero = true;
        for(FootData i : d){
            p = i.getPressure();
            leftPressureSum.add( p[0][0] + p[0][1] + p[0][2] + p[0][3] + p[0][4] + p[0][5] +
                    p[0][6] + p[0][7] + p[0][8] + p[0][9] + p[0][10] + p[0][11]);
            rightPressureSum.add( p[1][0] + p[1][1] + p[1][2] + p[1][3] + p[1][4] + p[1][5] +
                    p[1][6] + p[1][7] + p[1][8] + p[1][9] + p[1][10] + p[1][11]);
            if((int)leftPressureSum.get((leftPressureSum.size()-1)) != 0){
                l_zero = false;
                for(int k = 0; k < 12; k++) {
                    tmp_l.add(p[0][k]);
                }
            }
            else{
                if(l_zero) continue;
                leftPressure.add((ArrayList<Integer>) tmp_l.clone());
                tmp_l.clear();
                l_zero = true;
                step++;
            }
            if((int)rightPressureSum.get((rightPressureSum.size()-1))!=0){
                r_zero = false;
                for(int k=0;k<12;k++) {
                    tmp_r.add(p[1][k]);
                }
            }
            else{
                if(r_zero) continue;
                rightPressure.add((ArrayList<Integer>) tmp_r.clone());
                tmp_r.clear();
                r_zero = true;
                step++;
            }
        }
        tmp_l.clear();
        tmp_r.clear();
    }

    /**
     * 나눠진 걸음의 길이 정규화(각 센서가 10단계를 가지도록)
     */
    private void  normalizeStepLength(){
        int start[] = {0,1,2,3,4,5,6,7,8,9,10,11};
        int sum[] = {0,0,0,0,0,0,0,0,0,0,0,0};
        int len = 0;
        ArrayList<ArrayList<Integer>> tmp = new ArrayList<ArrayList<Integer>>();
        ArrayList ary = new ArrayList();
        for(ArrayList<Integer> i : leftPressure){
            len = i.size() /12;
            if(len == len_standard) tmp.add((ArrayList<Integer>)i.clone());
            else if(len > len_standard){
                int x = len/len_standard;
                int y = len%len_standard;
                int check = 0;
                start[0] = 0; start[1] = 1; start[2] = 2; start[3] = 3; start[4] = 4; start[5] = 5; start[6] = 6; start[7] = 7; start[8] = 8; start[9] = 9; start[10] = 10; start[11] = 11;
                for(int index = 0; index < len_standard; index++){
                    sum[0] = 0; sum[1] = 0; sum[2] = 0; sum[3] = 0; sum[4] = 0; sum[5] = 0; sum[6] = 0; sum[7] = 0; sum[8] = 0; sum[9] = 0; sum[10] = 0; sum[11] = 0;
                    if(y == check){
                        for(int k = 0; k < x; k++){
                            sum[0]+= i.get(start[0]); start[0]+= 12; sum[1]+= i.get(start[1]); start[1]+= 12; sum[2]+= i.get(start[2]); start[2]+= 12;
                            sum[3]+= i.get(start[3]); start[3]+= 12; sum[4]+= i.get(start[4]); start[4]+= 12; sum[5]+= i.get(start[5]); start[5]+= 12;
                            sum[6]+= i.get(start[6]); start[6]+= 12; sum[7]+= i.get(start[7]); start[7]+= 12; sum[8]+= i.get(start[8]); start[8]+= 12;
                            sum[9]+= i.get(start[9]); start[9]+= 12; sum[10]+= i.get(start[10]); start[10]+= 12; sum[11]+= i.get(start[11]); start[11]+= 12;
                        }
                        ary.add(sum[0]/x); ary.add(sum[1]/x); ary.add(sum[2]/x); ary.add(sum[3]/x); ary.add(sum[4]/x); ary.add(sum[5]/x);
                        ary.add(sum[6]/x); ary.add(sum[7]/x); ary.add(sum[8]/x); ary.add(sum[9]/x); ary.add(sum[10]/x); ary.add(sum[11]/x);

                    }
                    else{
                        for(int k = 0; k <= x; k++){
                            sum[0]+= i.get(start[0]); start[0]+= 12; sum[1]+= i.get(start[1]); start[1]+= 12; sum[2]+= i.get(start[2]); start[2]+= 12;
                            sum[3]+= i.get(start[3]); start[3]+= 12; sum[4]+= i.get(start[4]); start[4]+= 12; sum[5]+= i.get(start[5]); start[5]+= 12;
                            sum[6]+= i.get(start[6]); start[6]+= 12; sum[7]+= i.get(start[7]); start[7]+= 12; sum[8]+= i.get(start[8]); start[8]+= 12;
                            sum[9]+= i.get(start[9]); start[9]+= 12; sum[10]+= i.get(start[10]); start[10]+= 12; sum[11]+= i.get(start[11]); start[11]+= 12;
                        }
                        ary.add(sum[0]/(x+1)); ary.add(sum[1]/(x+1)); ary.add(sum[2]/(x+1)); ary.add(sum[3]/(x+1)); ary.add(sum[4]/(x+1)); ary.add(sum[5]/(x+1));
                        ary.add(sum[6]/(x+1)); ary.add(sum[7]/(x+1)); ary.add(sum[8]/(x+1)); ary.add(sum[9]/(x+1)); ary.add(sum[10]/(x+1)); ary.add(sum[11]/(x+1));
                        check++;
                    }
                }
                tmp.add((ArrayList<Integer>)ary.clone());
                ary.clear();
            }
        }
        leftPressure.clear();
        leftPressure.addAll(tmp);
        tmp.clear();

        for(ArrayList<Integer> i : rightPressure){
            len = i.size()/12;
            if(len == len_standard)  tmp.add((ArrayList<Integer>)i.clone());
            else if(len > len_standard){
                int x = len/len_standard;
                int y = len%len_standard;
                int check = 0;
                start[0] = 0; start[1] = 1; start[2] = 2; start[3] = 3; start[4] = 4; start[5] = 5; start[6] = 6; start[7] = 7; start[8] = 8; start[9] = 9; start[10] = 10; start[11] = 11;
                for(int index = 0; index < len_standard; index++){
                    sum[0] = 0; sum[1] = 0; sum[2] = 0; sum[3] = 0; sum[4] = 0; sum[5] = 0; sum[6] = 0; sum[7] = 0; sum[8] = 0; sum[9] = 0; sum[10] = 0; sum[11] = 0;

                    if(y == check){
                        for(int k = 0; k < x; k++){
                            sum[0]+= i.get(start[0]); start[0]+= 12; sum[1]+= i.get(start[1]); start[1]+= 12; sum[2]+= i.get(start[2]); start[2]+= 12;
                            sum[3]+= i.get(start[3]); start[3]+= 12; sum[4]+= i.get(start[4]); start[4]+= 12; sum[5]+= i.get(start[5]); start[5]+= 12;
                            sum[6]+= i.get(start[6]); start[6]+= 12; sum[7]+= i.get(start[7]); start[7]+= 12; sum[8]+= i.get(start[8]); start[8]+= 12;
                            sum[9]+= i.get(start[9]); start[9]+= 12; sum[10]+= i.get(start[10]); start[10]+= 12; sum[11]+= i.get(start[11]); start[11]+= 12;
                        }
                        ary.add(sum[0]/x); ary.add(sum[1]/x); ary.add(sum[2]/x); ary.add(sum[3]/x); ary.add(sum[4]/x); ary.add(sum[5]/x);
                        ary.add(sum[6]/x); ary.add(sum[7]/x); ary.add(sum[8]/x); ary.add(sum[9]/x); ary.add(sum[10]/x); ary.add(sum[11]/x);
                    }
                    else{
                        for(int k = 0; k <= x; k++){
                            sum[0]+= i.get(start[0]); start[0]+= 12; sum[1]+= i.get(start[1]); start[1]+= 12; sum[2]+= i.get(start[2]); start[2]+= 12;
                            sum[3]+= i.get(start[3]); start[3]+= 12; sum[4]+= i.get(start[4]); start[4]+= 12; sum[5]+= i.get(start[5]); start[5]+= 12;
                            sum[6]+= i.get(start[6]); start[6]+= 12; sum[7]+= i.get(start[7]); start[7]+= 12; sum[8]+= i.get(start[8]); start[8]+= 12;
                            sum[9]+= i.get(start[9]); start[9]+= 12; sum[10]+= i.get(start[10]); start[10]+= 12; sum[11]+= i.get(start[11]); start[11]+= 12;
                        }
                        ary.add(sum[0]/(x+1)); ary.add(sum[1]/(x+1)); ary.add(sum[2]/(x+1)); ary.add(sum[3]/(x+1)); ary.add(sum[4]/(x+1)); ary.add(sum[5]/(x+1));
                        ary.add(sum[6]/(x+1)); ary.add(sum[7]/(x+1)); ary.add(sum[8]/(x+1)); ary.add(sum[9]/(x+1)); ary.add(sum[10]/(x+1)); ary.add(sum[11]/(x+1));
                        check++;
                    }
                }
                tmp.add((ArrayList<Integer>)ary.clone());
                ary.clear();
            }
        }
        rightPressure.clear();
        rightPressure.addAll(tmp);
        tmp.clear();
    }

    /**
     * 모든 걸음에 대해 평균계산
     * @return feetPressure : 양 발의 압력 변화 배열
     */
    private double[][] featureExtract(){
        int leftPressureSumPerSensor[] = new int[12*len_standard];
        int rightPressureSumPerSensor[] = new int[12*len_standard];
        double [][]feetPressure = new double[2][12*len_standard];
        int len = leftPressure.size() < rightPressure.size()?leftPressure.size():rightPressure.size();
        for(int i = 0; i < len; i++){
            for(int j = 0;j < 120; j++)
            {
                leftPressureSumPerSensor[j]+= leftPressure.get(i).get(j);
                rightPressureSumPerSensor[j]+= rightPressure.get(i).get(j);
            }
        }
        for(int j = 0; j < 120; j++)
        {
            feetPressure[0][j] = leftPressureSumPerSensor[j]/(double)len;
            feetPressure[1][j] = rightPressureSumPerSensor[j]/(double)len;
        }
        leftPressure.clear();
        rightPressure.clear();
        return feetPressure;
    }

    /**
     * 양 발 무게중심 편향 계산
     * @return weight : 양 발 무게중심 편향 배열
     */
    private double[] calculateDifferencePressures(){
        int len = (int)Math.ceil(leftPressureSum.size()/10.0);
        double []weight = new double[len+1];
        int sumL = 0, sumR = 0, index = 1, i;
        for(i = 0; i < leftPressureSum.size()-10; i+=10){
            sumL = 0; sumR = 0;
            for(int j = i; j < i+10; j++){
                sumL += leftPressureSum.get(j);
                sumR += rightPressureSum.get(j);
            }
            weight[index] = (sumR/10.0) - (sumL/10.0);
            weight[index] = (weight[index] + 180.0)/360.0;
            index++;
        }
        sumL = 0; sumR = 0;
        int cnt=0;
        for(int j = i; j < leftPressureSum.size(); j++){
            sumL+= leftPressureSum.get(j);
            sumR+= rightPressureSum.get(j);
            cnt++;
        }
        weight[index] = (sumR/(double)cnt) - (sumL/(double)cnt);
        weight[index] = (weight[index] + 180.0)/360.0;
        return weight;
    }

    /**
     * 걷기 압력 값을 가공하여 결과 리턴
     * @return T
     */
    public ReturnDataType getResult() {
        normalizeStepLength();
        ReturnDataType T = new ReturnDataType();
        T.step = step;
        double[][] feetPressure = featureExtract();
        T.leftPressure = feetPressure[0];
        T.rightPressure = feetPressure[1];
        T.feetWeightBias = calculateDifferencePressures();
        return T;
    }

    /**
     * 걷기 특징값 반환 데이터
     * leftPressure : 왼발의 압력값 변화
     * rightPressure : 오른발의 압력값 변화
     * step : 걸음수
     * feetWeightBias : 양 발의 무게중심 편향(샘플 10개 당)
     */
    public static class ReturnDataType{
        private ReturnDataType() {}

        public double []leftPressure;
        public double []rightPressure;
        public int step;
        public double []feetWeightBias;
    }
}
