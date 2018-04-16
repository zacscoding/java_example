package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zacconding
 * @Date 2018-04-08
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pair<F,S> {
    private F fisrt;
    private S second;
}
