package com.jaehee.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void staticPressureProcessorTest() {
        //random data produce
        Random random = new Random();
        FootData []d = new FootData[140];
        int index = 0;
        int[]l_pressure = new int[12];
        int[] r_pressure = new int[12];
        for(index = 0; index < d.length; index++) {
            d[index] = new FootData();
            for (int j = 0; j < 12; j++) {
                l_pressure[j] = random.nextInt(15);
                r_pressure[j] = random.nextInt(15);
            }
            d[index].setData(index, l_pressure, r_pressure);
        }

        //data processing test
        StaticPressureProcessor model = new StaticPressureProcessor();
        model.process(d);
        double[] res = model.getResult();
        System.out.println("1, 2, 3, disease");
        for(double i : res){
            System.out.print(i + " ");
        }
    }

    @Test
    public void footstepPressureProcessorTest(){
        //random data produce
        Random random = new Random();
        FootData []d = new FootData[2000];
        int[] zero = new int[60];
        for(int i = 0; i<zero.length; i++)
            zero[i] = random.nextInt(2000);
        Arrays.sort(zero);
        int z = 0;
        int []l_pressure = new int[12];
        int[] r_pressure = new int[12];
        for(int index = 0; index < d.length; index++) {
            for (int j = 0; j < 12; j++) {
                l_pressure[j] = random.nextInt(15);
                r_pressure[j] = random.nextInt(15);
            }
            if( z < 60 && index == zero[z]) {
                z++;
                for(int j = 0; j < 12; j++) {
                    l_pressure[j] = 0;
                    r_pressure[j] = 0;
                }
            }
            d[index] = new FootData(index, l_pressure, r_pressure);
        }

        //data processing test
        FootStepPressureProcessor model = new FootStepPressureProcessor();
        model.process(d);
        FootStepPressureProcessor.ReturnDataType res = model.getResult();
        System.out.println("leftPressure : ");
        for(double i : res.leftPressure){
                System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("rightPressure : ");
        for(double i : res.rightPressure){
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("걸음 수 : "+res.step);
        System.out.println("두 발 무게중심 편향 : ");
        for(double i : res.feetWeightBias){
                System.out.print(i + " ");
        }
    }
}