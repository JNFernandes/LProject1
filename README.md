# LProject1

This project was done in colaboration with **more 4 elements**.

This is a simple project to get in touch with JAVA language and its syntax. The objective of this project was to enable th user to analyze, compare and perform predictions concerning the development of the pandemic situation of COVID-19. To that concern, the user had to give input files containing the cumulative and the total daily data due to COVID-19 (date, number of infected, number of infected, number of hospitalized , number of ICU and number of deaths) as well as the files containing a definition of a transition matrix which allowed to make some predictions. 

In this way, the application allows to do the following functionalities:
  - visualize the total/cumulative data for a specific period given by the used considering three temporal analysis (diary, weekly and monthly)
  - Compare total data/cumulative data for two specific periods defined by the user
  - Perform predictons of daily total cases for a specific day given by the user as well as the estimation of the number of days until reach the condition state of "dead".
 
The following figure explains the dependencies of the created app.

![image](https://user-images.githubusercontent.com/45408654/159162931-2e8d3a97-5052-4a7b-9502-37e982783926.png)

The application allows two types of modes: interactive and not-interactive. The not interactive mode is run by the command line with the corresponding needed arguments. 

**NOT-INTERACTIVE MODE**

As shown in the figure, this mode allows the user to execute three different distinct modes: total mode, mode without predictions and only with predictions.

In order to understand how to run the not-interactive mode, a --help comand was introduced to help the users to understand the arguments needed for its execution. 

![image](https://user-images.githubusercontent.com/45408654/159162618-d7a1bb74-9d7a-42e9-87bc-f4a1fe5c455e.png)

The parameters have to be introduced in a specified order, otherwise it will print some error messages. In the end, the files are saved in a txt file with the name given by the last parameter. 

**INTERACTIVE MODE**

The interactive mode has implemented the main menu with four options including an option to exit the application, see figure bellow.

![image](https://user-images.githubusercontent.com/45408654/159162984-c6150864-e9d0-4995-afb8-46efecb09981.png)


The fileread submenu allowed the possibility of adding new files with different dates to the ones already created and loaded, see figure bellow:

![image](https://user-images.githubusercontent.com/45408654/159163285-2c2bb2f6-8a18-475e-af20-eb5f46f1cd3d.png)

The visualization submenu allows to see new cases or total cases until a specified date. Each one needs of a inicial date, final date introduced by the user. The user still can ask for daily, weekly or monthly resolution and see the health states.

The comparative analysis submenu hosts the data comparison methods. These allow options for new  cases or total cases. Each needs a start date and an end date, requested from the user. It also asks about the health states to be compared. Afterwards, it prints on the console the requested comparison in a daily resolution, including averages and standard deviations of total days for each health state.

The forecast submenu hosts the forecasting methods of estimating total cases in a
day and expected number of days until death. For the first one, it is necessary to enter a date
future to the last read side of the total data file. The console prints the forecasts for the five health states for the day entered. In the second method, the console prints a forecast
of the expected number of days to death based on the intensity matrix.

All input requested from the user, including non-interactive mode arguments,
are subject to validation. At the exit of each main menu method, with the exception of the read one of files, the user has the option to save the analysis data in csv file. All the
methods created have unit tests associated with it, which have been successfully executed.












