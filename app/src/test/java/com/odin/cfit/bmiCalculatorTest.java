package com.odin.cfit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import com.odin.cfit.model.UserInformation;

import org.junit.Test;

public class bmiCalculatorTest {

    @Test
    public void shouldBMIlessthanthirthy(){
        //arrange
        double weight = 0;
        double height = 1.73; //meters
        String expectedBMI = "Underweight";

        //act
        String actual = UserInformation.calculateBMI(weight, height);

        //assert
        assertThat(expectedBMI, equalTo(actual));
    }

    @Test
    public void shouldNotGreaterthenThirty(){

        //arrange
        double weight = 50; //kg
        double height = 1.524; //meters
        String expectedBMI = "Obese";

        //act
        String actual = UserInformation.calculateBMI(weight, height);

        //assert
        assertThat(expectedBMI, equalTo(actual));

    }

    @Test
    public void shoulduserinsertnegativevalue(){

        double weight = 90; //kg
        double height = -3; //meters
        String expectedBMI = "Underweight";

        //act
        String actual = UserInformation.calculateBMI(weight, height);

        //assert
        assertThat(expectedBMI, equalTo(actual));

    }

}

