package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;

public class CountByOrganizationCommand extends Commands{
    public  CountByOrganizationCommand(){
        super("count_by_organization");
    }

    @Override
    public void execute() {
        HistoryWorker.getInstance().countByOrganization();
    }
}
