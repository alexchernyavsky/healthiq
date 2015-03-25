import com.healthiq.business.BloodSugarRateCalculator;
import com.healthiq.info.ActivityInfo;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for Blood Sugar Calculator
 *
 * @author Alex Chernyavsky
 */

public class BloodSugarRateCalculatorAutoTest {

	public static String CONFIG_PATH = "../test/config";
	private static String testLoadTwoFoodActivitiesJson = "";
	private static String testLoadOneFoodOneExActivitiesJson = "";
	private static String testLoadTwoFoodsWithNoActivityGap = "";

	@BeforeClass
	public static void oneTimeSetUp() {
		try {
			//set up all data testing JSON's at once
			testLoadTwoFoodActivitiesJson = new String(Files.readAllBytes(Paths.get(CONFIG_PATH + File.separator + "two_foods.json")));
			testLoadOneFoodOneExActivitiesJson = new String(Files.readAllBytes(Paths.get(CONFIG_PATH + File.separator + "one_food_one_ex.json")));
			testLoadTwoFoodsWithNoActivityGap = new String(Files.readAllBytes(Paths.get(CONFIG_PATH + File.separator + "two_foods_with_no_activity_gap.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void calculate_using_two_food_activities_success() {

		ArrayList<ActivityInfo> aInfoList = (ArrayList) ActivityInfo.fromJSONArray(testLoadTwoFoodActivitiesJson);

		Assert.assertTrue(aInfoList != null);

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(CONFIG_PATH);
			calculator.buildTimeLines(aInfoList);

			List<Double> bloodSugarTimeLine = calculator.getBloodSugarTimeLine();
			Assert.assertTrue(bloodSugarTimeLine != null);
			Assert.assertTrue(!bloodSugarTimeLine.isEmpty());

			List<Integer> glycationTimeLine = calculator.getGlycationTimeLine();
			Assert.assertTrue(glycationTimeLine != null);
			Assert.assertTrue(!glycationTimeLine.isEmpty());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	@Test
	public void calculate_using_one_food_one_ex_activities_success() {


		ArrayList<ActivityInfo> aInfoList = (ArrayList) ActivityInfo.fromJSONArray(testLoadOneFoodOneExActivitiesJson);

		Assert.assertTrue(aInfoList != null);

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(CONFIG_PATH);
			calculator.buildTimeLines(aInfoList);

			List<Double> bloodSugarTimeLine = calculator.getBloodSugarTimeLine();
			Assert.assertTrue(bloodSugarTimeLine != null);
			Assert.assertTrue(!bloodSugarTimeLine.isEmpty());

			List<Integer> glycationTimeLine = calculator.getGlycationTimeLine();
			Assert.assertTrue(glycationTimeLine != null);
			Assert.assertTrue(!glycationTimeLine.isEmpty());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	@Test
	public void calculate_using_two_foods_and_no_activities_gap_success() {

		ArrayList<ActivityInfo> aInfoList = (ArrayList) ActivityInfo.fromJSONArray(testLoadTwoFoodsWithNoActivityGap);

		Assert.assertTrue(aInfoList != null);

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(CONFIG_PATH);
			calculator.buildTimeLines(aInfoList);

			List<Double> bloodSugarTimeLine = calculator.getBloodSugarTimeLine();
			Assert.assertTrue(bloodSugarTimeLine != null);
			Assert.assertTrue(!bloodSugarTimeLine.isEmpty());

			List<Integer> glycationTimeLine = calculator.getGlycationTimeLine();
			Assert.assertTrue(glycationTimeLine != null);
			Assert.assertTrue(!glycationTimeLine.isEmpty());
		} catch (Exception e) {
			Assert.assertTrue(false);
		}

	}

	@Test
	public void retrieve_list_of_exercise_activities_success() {

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(CONFIG_PATH);

			List<ActivityInfo> aList = calculator.getExerciseActivities();
			Assert.assertTrue(aList != null);
			Assert.assertTrue(aList.size() == 6);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}


	}

	@Test
	public void retrieve_list_of_food_activities_success() {

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(CONFIG_PATH);

			List<ActivityInfo> aList = calculator.getFoodActivities();
			Assert.assertTrue(aList != null);
			Assert.assertTrue(aList.size() == 112);
		} catch (Exception e) {
			Assert.assertTrue(false);
		}


	}

}
