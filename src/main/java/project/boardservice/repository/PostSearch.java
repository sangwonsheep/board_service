package project.boardservice.repository;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class PostSearch {

    private String title;


    public PostSearch(String title) {
        this.title = title;
    }
}
