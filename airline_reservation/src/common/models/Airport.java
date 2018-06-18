package common.models;

public class Airport {
    private String airportName;
    private String airportCode;
    private Object[] currentWeather;
    private int currentWeatherIteration;
    private int airportDelayTime;
    private String FAAWeatherString;
    private String FAATemp;
    private String FAADelayTime;

    public Airport(String airportName, String airportCode, Object[] currentWeather,
                   int currentWeatherIteration, int airportDelayTime) {
        this.airportName = airportName;
        this.airportCode = airportCode;
        this.currentWeather = currentWeather;
        this.currentWeatherIteration = currentWeatherIteration;
        this.airportDelayTime = airportDelayTime;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public Object[] getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(Object[] currentWeather) {
        this.currentWeather = currentWeather;
    }

    public int getCurrentWeatherIteration() {
        return currentWeatherIteration;
    }

    public void setTemperature(int iteration) {
        this.currentWeatherIteration = iteration;
    }

    public int getAirportDelayTime() {
        return airportDelayTime;
    }

    public void setAirportDelayTime(int airportDelayTime) {
        this.airportDelayTime = airportDelayTime;
    }

    @Override
    public String toString() {
        this.currentWeatherIteration++;
        if (this.currentWeatherIteration >= this.currentWeather.length) {
            this.currentWeatherIteration = 0;
        }
        return this.airportName + " (" + this.airportCode + ")\nCurrent Weather:\t" + this.currentWeather[this.currentWeatherIteration] + "\nAirport Delay Time:\t" + this.airportDelayTime + "\n";
    }
}
