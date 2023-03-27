package com.odin.cfit;

import static org.junit.Assert.*;

import com.odin.cfit.model.UserInformation;

import org.junit.Test;

public class UserCalculationTest {

    @Test
    public void shouldCalculateRequiredCalorie(){
        //arrange
        double goalWeight = 176; //in lbs
        double expectedResult = 2112;
        //act
        double actual = UserInformation.CalculateRequiredCalories(goalWeight);

        //assert
        assertEquals(expectedResult, actual, 0.01 );
        //assertEquals(expectedBMI, actualBMI, 0.01);

    }

    @Test
    public void shouldCalculateWeightforecast(){
        //arrange
        //weight in lbs
        double weight = 198;
        double goalWeight = 176;
        double expectedResult = 55.47;

        //act
        double actual = UserInformation.CalculateWeightForecast(weight, goalWeight);

        //assert

        assertEquals(expectedResult, actual, 0.01);
    }

}