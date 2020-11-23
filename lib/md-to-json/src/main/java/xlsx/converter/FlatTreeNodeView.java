package xlsx.converter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class FlatTreeNodeView {

    private final Boolean isRoot;

    private final Boolean hasChildren;

    private final String name;

    @Setter
    private Set<FlatTreeNodeView> children;
}
