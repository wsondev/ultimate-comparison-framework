# Choose Framework to parse Data from Excel Sheet

**User Story:** #97

The platform comparison data from Excel should be displayed in the UI. Excel data can be stored as MS Office document with xlsx extendsion or as Open Office document with ods extension. For this task data will be transformed to JSON, because ultimate comparison app already uses JSON for rendering data.
There are three options for transformation

## Considered Alternatives

- apache-poi: Framework to parse data from Microsoft Office Documents only.
- odftoolkit: Framework to parse Open Office Documents.
- native xml parsing: Open Office document has xml structure inside. Extract ods file an parse xml structure with parser like dom.

## Decision Outcome

`apache-poi`

Because it's less complicated to get data from Excel sheet.

## Pros and Cons of Alternatives

### `odftoolkit`

* Maintenance is not up to date. Some many classes are deprecated.

### `apache-poi`

* Powerfull tool, does allow parse Microsoft documents

### `native xml parsing`

* XML parsing possible with odftoolkit only. First an ods file has to be renamed to zip file and extracted.
* XML structure will be parsed as whole document. It produces too much effort to get datt out of XML structure.
