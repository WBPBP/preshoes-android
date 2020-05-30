package com.jaehee.model;

/**
 * 정적 족저압 검사 후의 값을 입력받아 데이터 가공을 진행하는 클래스입니다.
 * 발에 있는 센서를 다음과 같이 그룹화하였습니다. (part1, part2, part3, part4 라고 칭하겠습니다.)
 * {"part1" : [0], "part2" : [1, 2, 3, 4], "part3" : [5, 6, 7], "part4" : [8, 9, 10, 11]}
 */
public class StaticPressureProcessor {
    private int[] leftFootPressureSumPerPart;   // 왼발의 센서를 4 파트로 나누어 파트별로 압력 값을 더함
    private int[] rightFootPressureSumPerPart;  // 오른발의 센서를 4 파트로 나누어 파트별로 압력 값을 더함
    private double verticalWeightBias_Left;     // 왼발의 앞/뒤꿈치 무게중심 편향
    private double verticalWeightBias_Right;    // 오른발의 앞/뒤꿈치 무게중심 편향
    private double horizontalWeightBias;        // 두 발의 무게중심 편향
    private double heelPressureDifference;      // 후에 척추측만증 여부를 알아보기 위한 두 발의 뒤꿈치 압력 값 차이
    private int len;                            // 들어오는 데이터의 길이 저장

    public StaticPressureProcessor(){
        heelPressureDifference = 0;
        len = 0;
        leftFootPressureSumPerPart = new int[4];
        rightFootPressureSumPerPart = new int[4];
    }

    /**
     *  FootData 배열을 입력받아 각 발의 모든 센서를 파트별로 합하여 배열에 저장
     * @param d 정지한 경우, 센서로부터 입력받은 데이터 배열
     */
    public  void process(FootData[]d) {
        len = d.length;
        for (FootData i : d) {
            int[][] p = i.getPressure();
            leftFootPressureSumPerPart[0] += p[0][0];
            leftFootPressureSumPerPart[1] += p[0][1] + p[0][2] + p[0][3] + p[0][4];
            leftFootPressureSumPerPart[2] += p[0][5] + p[0][6] + p[0][7];
            leftFootPressureSumPerPart[3] += p[0][8] + p[0][9] + p[0][10] + p[0][11];
            rightFootPressureSumPerPart[0] += p[1][0];
            rightFootPressureSumPerPart[1] += p[1][1] + p[1][2] + p[1][3] + p[1][4];
            rightFootPressureSumPerPart[2] += p[1][5] + p[1][6] + p[1][7];
            rightFootPressureSumPerPart[3] += p[1][8] + p[1][9] + p[1][10] + p[1][11];
        }
    }

    /**
     * 양 발의 앞/뒤꿈치 무게 중심 편향 계산
     */
    private void calculatePartPressureDifference(){
        leftFootPressureSumPerPart[0] /= len; leftFootPressureSumPerPart[1] /= len; leftFootPressureSumPerPart[2] /= len; leftFootPressureSumPerPart[3] /= len;
        rightFootPressureSumPerPart[0] /= len; rightFootPressureSumPerPart[1] /= len; rightFootPressureSumPerPart[2] /= len; rightFootPressureSumPerPart[3] /= len;
        verticalWeightBias_Right = rightFootPressureSumPerPart[3] - rightFootPressureSumPerPart[1];
        verticalWeightBias_Left = leftFootPressureSumPerPart[3] - leftFootPressureSumPerPart[1];
        verticalWeightBias_Left = (verticalWeightBias_Left + 60)/120.0;
        verticalWeightBias_Right = (verticalWeightBias_Right + 60)/120.0;
    }

    /**
     * 두 발의 무게중심 편향 계산
     */
    private void calculatePressureDifference(){
        horizontalWeightBias = (rightFootPressureSumPerPart[0] + rightFootPressureSumPerPart[1] + rightFootPressureSumPerPart[2] + rightFootPressureSumPerPart[3]) - (leftFootPressureSumPerPart[0] + leftFootPressureSumPerPart[1] + leftFootPressureSumPerPart[2] + leftFootPressureSumPerPart[3]);
        horizontalWeightBias = (horizontalWeightBias + 180)/360.0;
    }

    /**
     * 두 발의 뒤꿈치 차이를 계산
     */
    private void calculateheelPressureDifferencee(){
        heelPressureDifference = Math.abs(rightFootPressureSumPerPart[3] - leftFootPressureSumPerPart[3]);
    }

    /**
     * 정적 족저압의 결과 값 가공하여 리턴
     * @return res
     */
    public double[] getResult(){
        calculatePartPressureDifference();
        calculatePressureDifference();
        calculateheelPressureDifferencee();
        double[] res ={verticalWeightBias_Left, verticalWeightBias_Right, horizontalWeightBias, heelPressureDifference };
        return res;
    }
}
