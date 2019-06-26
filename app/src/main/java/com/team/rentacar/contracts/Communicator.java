package com.team.rentacar.contracts;

public interface Communicator {
    interface homeNavigator{

        void navigateToOtherActivities(int id,String vendor);
    }
    interface IRent{
        void rentIt(String id);
    }
    interface IBookings{
        void cancel(String id);
    }
}
