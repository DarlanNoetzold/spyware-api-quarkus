package tech.noetzold.model;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import java.util.List;

public class UploadForm {
    private List<InputPart> file;

    public List<InputPart> getFile() {
        return file;
    }

    public void setFile(List<InputPart> file) {
        this.file = file;
    }
}