package com.thenaglecode.sendalist.shared;

import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class is used to determine the origin of the persistent change transaction, whether the request is from a user
 * in our system, what ip, etc... common usage is to determine whether a change is valid or forbidden for a given
 * session id.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 14/07/12
 * Time: 6:05 PM
 */
public class OriginatorOfPersistentChange {
    String ip;
    UserAccount user;


    OriginatorOfPersistentChange(HttpServletRequest req) {
        ip = req.getRemoteAddr();
    }

    private UserAccount getUserAccount() {
        return user;
    }

    public static OriginatorOfPersistentChange getContext(HttpServletRequest req) {
        OriginatorOfPersistentChange context = new OriginatorOfPersistentChange(req);
        HttpSession session = req.getSession();
        if(session != null){
            //todo set the current user
        }
        return context;
    }
}
