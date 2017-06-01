package com.schedule.generator.dto;

import java.io.Serializable;


public final class Response implements Serializable {

    private static final long serialVersionUID = 4871154894089046318L;
    
    private boolean status;
    private Object data;
    
    public Response() {
		// dummy
	}

    private Response(final boolean status, final Object data) {
        this.status = status;
        this.data = data;
    }
    
    /**
     * @return the status
     */
    public boolean getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	public static Response successResponse(Object data) {
        return new Response(true, data);
    }
	
	public static Response successResponse() {
        return successResponse(null);
    }
    
	public static Response errorResponse(Object data) {
        return new Response(false, data);
    }
	
	public static Response errorResponse() {
        return errorResponse(null);
    }
}
