package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.ToJson;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 8:14 PM
 * <p/>
 * Tasks must be created in an existing tasklist
 */
public class Task implements Comparable<Task>, ToJson, Processable {

    private boolean done;
    private long created;
    private String summary = null;
    private String picUrl = null;
    public static final String FIELD_SUMMARY = "summary"; //writable (rename the task via the "rename" variable}
    public static final String FIELD_DONE = "done"; //writable
    public static final String FIELD_PIC_URL = "picurl"; //writable
    public static final String FIELD_CREATED = "created"; //read-only


    public Task(String summary) {
        this();
        this.summary = summary;
        this.done = false;
    }

    public Task() {
        created = System.currentTimeMillis();
    }

    public Task setDone(boolean done) {
        this.done = done;
        return this;
    }

    public boolean getDone() {
        return done;
    }

    public long getCreated() {
        return created;
    }

    public String getCreatedRfc3339() {
        //todo implement with jodaTime package
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        return "Not yet implemented";
    }

    public Task setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Task setPicUrl(String url){
        this.setPicUrl(url);
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getDone() ? " " : "X").append("] ");
        sb.append(getSummary());
        return sb.toString();
    }

    public String toHtmlListElement() {
        return "<li>" + toString() + "</li>";
    }

    @Override
    public String processTransaction(JsonObject tx) {

        boolean changed = false;

        for (Map.Entry<String, JsonElement> entry : tx.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            boolean couldNotParse = false;
            boolean unsupported = false;
            String valueAsString = null;
            try {
                valueAsString = value.getAsString();
            } catch (ClassCastException e) {
                couldNotParse = true;
            } catch (UnsupportedOperationException e) {
                unsupported = true;
            }

            if(FIELD_SUMMARY.equals(key)){
                //do nothing, handled by tasklist
            } else if(FIELD_DONE.equals(key)){
                boolean newDone = value.getAsBoolean();
                if(this.getDone() != newDone){
                    changed = true;
                    this.setDone(newDone);
                }
            } else if(FIELD_PIC_URL.equals(key)){
                if(couldNotParse || unsupported){
                    return "problem parsing " + FIELD_PIC_URL + " value for task transaction: " + value;
                }
                if(!valueAsString.equals(this.getPicUrl())){
                    changed = true;
                    setPicUrl(this.getPicUrl());
                }
            } else {
                return "did not understand field for task transaction: " + key;
            }
        }
        return (changed) ? null : Processable.Nop;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafeToPersist() {
        return summary != null;
    }

    @Override
    /** {@inheritDoc} */
    public int compareTo(Task o) {
        int compare = Long.valueOf(created).compareTo(o.getCreated());
        boolean safeToPersist = isSafeToPersist();
        if (compare != 0 || !safeToPersist) return compare;
        assert summary != null;
        compare = summary.compareTo(o.getSummary());
        if (compare != 0) return compare;
        compare = Boolean.valueOf(done).compareTo(o.getDone());
        return compare;
    }

    /**
     * this function will return an exact duplicate of this task unless you specify that you want a duplicate with a new
     * id (and creation time of now).
     */
    @NotNull
    public Task duplicate(boolean withNewCreationTime) {
        Task copy = new Task();
        if (withNewCreationTime) {
            copy.created = System.currentTimeMillis();
        } else {
            copy.created = this.created;
        }
        copy.setSummary(this.summary);
        copy.setDone(this.done);
        return copy;
    }

    /** {@inheritDoc} */
    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(FIELD_SUMMARY, this.getSummary());
            obj.put(FIELD_DONE, this.getDone());
            obj.put(FIELD_CREATED, this.getCreatedRfc3339());
            if(this.getPicUrl() != null) obj.put(FIELD_PIC_URL, this.getPicUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
