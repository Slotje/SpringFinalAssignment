package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Lob;


public class UpdateTaskFileRequest {
    @JsonProperty
    private byte[] file;

    @Lob
    public byte[] getFile() {
        return file;
    }
}
