package xlsx.converter;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public class TreeNodeMapper implements Function<List<PointTableViewFlat>, FlatTreeNodeView> {

    public FlatTreeNodeView apply(final List<PointTableViewFlat> elements) {
        Set<PointTableViewFlat> rootNodeSet = elements.stream()
            .filter(e -> !isReferencedByAnotherTable(e, elements))
            .collect(toSet());

        if (rootNodeSet.size() != 1) {
            throw new RuntimeException("Could not calculate root node");
        }

        PointTableViewFlat root = rootNodeSet.stream().findFirst().get();
        FlatTreeNodeView rootNode = new FlatTreeNodeView(true, true, root.getSheetName());
        rootNode.setChildren(getChildren(root, elements));
        return rootNode;
    }

    private Boolean hasChildren(final PointTableViewFlat table) {
        return table.getRows()
            .stream()
            .anyMatch(r -> r.values()
                .stream()
                .anyMatch(PointTableViewCell::getHasSubTable));
    }

    private Boolean isReferencedByAnotherTable(final PointTableViewFlat table,
                                              final List<PointTableViewFlat> tables) {
        for (final PointTableViewFlat t : tables) {
            if (t.equals(table)) {
                continue;
            }
            boolean isReferenced = t.getRows()
                .stream()
                .flatMap(r -> r.values().stream())
                .filter(PointTableViewCell::getHasSubTable)
                .anyMatch(k -> k.getSubTableName().equals(table.getSheetName()));
            if (isReferenced) {
                return true;
            }
        }
        return false;
    }

    private Set<FlatTreeNodeView> getChildren(final PointTableViewFlat element,
                                             final List<PointTableViewFlat> elements) {
        Set<FlatTreeNodeView> result = new HashSet<>();
        for (final PointTableViewFlat t : elements) {
            if (t.equals(element)) {
                continue;
            }
            boolean isChild = element.getRows().stream()
                .flatMap(m -> m.values().stream())
                .filter(PointTableViewCell::getHasSubTable)
                .anyMatch(p -> p.getSubTableName().equals(t.getSheetName()));
            if (isChild) {
                FlatTreeNodeView child = new FlatTreeNodeView(false,
                    hasChildren(t),
                    t.getSheetName());
                result.add(child);
                if (child.getHasChildren()) {
                    child.setChildren(getChildren(t, elements));
                }
            }
        }
        return result;
    }
}
