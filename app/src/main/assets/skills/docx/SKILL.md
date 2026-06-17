---
name: docx
description: "Create, edit, and review Word documents (.docx). Supports document creation with footnotes, hyperlinks, and tables; editing existing files; adding comments and tracked changes; and Markdown-to-Word conversion. Use when the user mentions Word, .docx, or asks to create/edit a Word document."
---

# Part 1: Objectives

## Core Purpose

| Scenario | Tool | Why |
|----------|------|-----|
| **Create new documents** | docx-js (`npm install docx`) | Native footnotes, hyperlinks, bookmarks, complex tables, page numbers — no XML hacks needed |
| **Edit existing `.docx`** | python-docx | Open → modify → save |
| **Comments / tracked changes** | `DocxContext` + editing module | Handles 5 comment XML files automatically |
| **Markdown to Word** | `pandoc input.md -o output.docx` | One command |

## Core Principles

1. **docx-js for creation** — All new document creation uses the `docx` npm package. Do not use python-docx for creation.
2. **python-docx for editing** — Open existing files, modify content, save. Simple and sufficient.
3. **Editing module for comments/revisions** — All comment and tracked-change operations go through `DocxContext`. Do not hand-craft comment XML.
4. **Preserve formatting** — When editing existing documents, modify in place; do not rebuild from scratch.

## Source Principles

- **Template provided** = Form filler. Replace placeholders; do not redesign.
- **No template** = Designer. Design freely based on scenario.

---

# Part 2: Execution

## File Structure

```
docx/
├── SKILL.md
├── scripts/
│   ├── docx_lib/
│   │   ├── constants.py              → OOXML namespace definitions
│   │   └── editing/                  → Comment/revision module (Python)
```

## Dependencies

```bash
# Creation
npm install docx

# Editing & comments
pip install python-docx lxml
```

---

## Creation Workflow (docx-js)

Write a Node.js script, run with `node script.js`.

### Basic Document

```js
const { Document, Packer, Paragraph, TextRun, HeadingLevel,
        Header, Footer, PageNumber, NumberOfPages,
        AlignmentType, TabStopType, TabStopPosition,
        Table, TableRow, TableCell, WidthType, BorderStyle,
        ImageRun, ExternalHyperlink, InternalHyperlink,
        FootnoteReferenceRun, Bookmark,
        SectionType, PageOrientation,
        convertInchesToTwip } = require('docx');
const fs = require('fs');

(async () => {
const doc = new Document({
    sections: [{
        properties: {
            page: {
                margin: { top: 1440, bottom: 1440, left: 1440, right: 1440 },
            },
        },
        headers: {
            default: new Header({
                children: [new Paragraph({ text: "Document Title", alignment: AlignmentType.CENTER })],
            }),
        },
        footers: {
            default: new Footer({
                children: [new Paragraph({
                    alignment: AlignmentType.CENTER,
                    children: [
                        new TextRun("Page "),
                        new PageNumber(),
                        new TextRun(" of "),
                        new NumberOfPages(),
                    ],
                })],
            }),
        },
        children: [
            new Paragraph({
                text: "Document Title",
                heading: HeadingLevel.HEADING_1,
            }),
            new Paragraph({
                children: [
                    new TextRun({ text: "Normal body text. ", font: "Times New Roman", size: 24 }),
                    new TextRun({ text: "Bold text. ", bold: true }),
                    new TextRun({ text: "Italic text.", italics: true }),
                ],
            }),
        ],
    }],
});

const buffer = await Packer.toBuffer(doc);
fs.writeFileSync("/path/to/output.docx", buffer);
})();
```

### Table of Contents

```js
const { TableOfContents } = require('docx');

// Insert in section children — Word populates entries on open
new TableOfContents("Table of Contents", {
    hyperlink: true,           // entries are clickable
    headingStyleRange: "1-3",  // include Heading1 through Heading3
})
```

Requires headings to use built-in `HeadingLevel.HEADING_1/2/3` styles. After opening in Word, right-click TOC → "Update Field" to populate page numbers. Add a gray prompt paragraph after the TOC reminding users to refresh.

### Footnotes (Native — Recommended for Citations)

```js
const doc = new Document({
    footnotes: {
        1: { children: [new Paragraph("Smith et al., 2024. Deep Learning for NLP. Nature, 608, 1-12.")] },
        2: { children: [new Paragraph("Wang et al., 2023. Sentiment Analysis Survey. ACL, 45-60.")] },
    },
    sections: [{
        children: [
            new Paragraph({
                children: [
                    new TextRun("Deep learning has transformed NLP"),
                    new FootnoteReferenceRun(1),  // → superscript ¹ linking to footnote
                    new TextRun(". Recent surveys confirm this trend"),
                    new FootnoteReferenceRun(2),  // → superscript ²
                    new TextRun("."),
                ],
            }),
        ],
    }],
});
```

Word renders these as native footnotes — auto-numbered, at page bottom, clickable.

### Hyperlinks and Bookmarks

```js
// External hyperlink
new ExternalHyperlink({
    link: "https://example.com",
    children: [new TextRun({ text: "Click here", style: "Hyperlink" })],
})

// Internal bookmark + link (e.g., TOC → section jump)
// At target:
new Paragraph({
    children: [new Bookmark({ id: "sec1", children: [new TextRun("Chapter 1")] })],
    heading: HeadingLevel.HEADING_1,
})
// At reference:
new InternalHyperlink({
    anchor: "sec1",
    children: [new TextRun({ text: "See Chapter 1", style: "Hyperlink" })],
})
```

### Tables

```js
new Table({
    columnWidths: [4000, 4000],
    rows: [
        new TableRow({
            children: [
                new TableCell({ children: [new Paragraph("Header 1")] }),
                new TableCell({ children: [new Paragraph("Header 2")] }),
            ],
        }),
        new TableRow({
            children: [
                new TableCell({ children: [new Paragraph("Data 1")] }),
                new TableCell({ children: [new Paragraph("Data 2")] }),
            ],
        }),
    ],
})
```

For three-line tables: set `borders` on the Table with thick top/bottom, medium internal horizontal, no vertical lines.

### Subscript / Superscript

```js
new TextRun({ text: "H" }),
new TextRun({ text: "2", subScript: true }),
new TextRun({ text: "O" }),
// Renders: H₂O with native Word subscript
```

### Charts

docx-js does not support native Word charts. Use **matplotlib** to generate chart images, then insert with `ImageRun`:

```js
const imageBuffer = fs.readFileSync("/tmp/chart.png");
new Paragraph({
    children: [
        new ImageRun({ data: imageBuffer, transformation: { width: 500, height: 300 } }),
    ],
})
```

### Page Breaks and Sections

```js
// Page break
new Paragraph({ children: [new PageBreak()] })

// New section (e.g., landscape page)
// Each section is a separate entry in the sections array
sections: [
    { properties: { /* portrait */ }, children: [...] },
    { properties: { page: { orientation: PageOrientation.LANDSCAPE } }, children: [...] },
]
```

### Chinese Font Support

```js
new TextRun({
    text: "中文内容",
    font: { name: "SimSun", eastAsia: "SimSun" },
    size: 24, // 12pt = 24 half-points
})
```

---

## Editing Workflow (python-docx)

For modifying existing `.docx` files:

```python
from docx import Document

doc = Document('/path/to/existing.docx')

for para in doc.paragraphs:
    if 'old text' in para.text:
        for run in para.runs:
            run.text = run.text.replace('old text', 'new text')

doc.add_paragraph('New paragraph at the end.')
doc.save('/path/to/output.docx')
```

---

## Comments and Tracked Changes (Editing Module)

```python
import sys
sys.path.insert(0, '/path/to/docx/scripts')

from docx_lib.editing import (
    DocxContext,
    add_comment, reply_comment, resolve_comment, delete_comment,
    insert_paragraph, insert_text, propose_deletion,
    enable_track_changes
)

with DocxContext("input.docx", "output.docx") as ctx:
    add_comment(ctx, "paragraph containing target",
                "Your comment here", highlight="specific phrase")
    reply_comment(ctx, comment_id="0", reply="Noted.")
    resolve_comment(ctx, comment_id="0")

    enable_track_changes(ctx)
    insert_text(ctx, "The method was applied",
                after="method", new_text=" and materials")
    propose_deletion(ctx, "This paragraph should be removed",
                     target="should be removed")
```

**Key rules:**
- `para_text` must uniquely identify a paragraph
- `highlight` / `after` / `target` must appear exactly once (use `context` for disambiguation)
- DocxContext saves automatically on exit

---

## Markdown to Word

```bash
pandoc input.md -o output.docx
pandoc input.md --reference-doc=template.docx -o output.docx
```

---

# Part 3: Quality Standards

## Common Pitfalls (DO / DON'T)

**Subscript / Superscript**
- ❌ Unicode characters (`aₙ`, `x²`) — breaks in WPS
- ✅ `new TextRun({ text: "2", subScript: true })`

**Chinese fonts**
- ❌ Only setting western font — Chinese falls back to Calibri
- ✅ Set both: `font: { name: "SimSun", eastAsia: "SimSun" }`

**Citations — must use footnotes, not fake text**
- ❌ Plain text `[1,2,3,4]` dumped at end of paragraph — fake completeness, not clickable, not precise
- ✅ `new FootnoteReferenceRun(1)` at the exact point where the claim is made — one footnote per specific claim, native Word footnote, auto-numbered, clickable
- Each footnote must correspond to exactly one reference. Do not bulk-cite multiple sources for a vague statement.

**Image sizing**
- ❌ Omitting `transformation` — image may overflow page
- ✅ Always set `transformation: { width: 500, height: 300 }` (in pixels)

**First-line indent (Chinese)**
- ❌ Using spaces
- ✅ `indent: { firstLine: convertInchesToTwip(0.33) }` (≈ 2 Chinese characters)

---

## Delivery Standards

**A Word document with mediocre styling and average completeness is an unacceptable deliverable.**

### Language Consistency

Document language defaults to user's conversation language. All elements (headings, headers, footers, captions, footnotes) must use consistent language.

### Headers and Footers: Required by Default

- **Header**: Document title or section name
- **Footer**: `Page X of Y` using `PageNumber()` + `NumberOfPages()`

### Citations and References

For reports, papers, theses, and any document citing external data:
- ❌ Reference list at the end with zero in-text footnotes — decoration, not citation
- ❌ Bulk-dumping `[1,2,3,4]` at paragraph end — lazy, imprecise
- ✅ Use `FootnoteReferenceRun(N)` at the exact sentence making the claim
- ✅ One footnote = one specific source for one specific claim
- ✅ All references must be real and verifiable — fabrication is prohibited
- Format: Chinese → GB/T 7714; English → APA

### Word Count and Page Count

| Requirement | Standard |
|-------------|----------|
| Specified word count | Within ±20% |
| Specified page count | Hit closely |
| Range given | Fall within |
| Minimum | Do not exceed 2x |

### Outline Adherence

- **User provided outline**: Follow strictly
- **No outline**: Standard structure by type (Academic: Intro→Methods→Results→Discussion→Conclusion; Business: Summary→Analysis→Recommendations)

### Scenario Completeness

Think one step ahead. Include elements naturally required:
- **Exam**: Name/ID fields, point values, scoring table
- **Contract**: Signature areas, date, number, attachments
- **Meeting minutes**: Attendees, action items, owners, next date
- **Resume**: Contact header, date format, skills sections

### Design Principles

**Color**: Low-saturation palettes. Pick direction by scenario (earth tones for nature, cool grays for tech, navy/burgundy for academic). No warm/cool mixing.

**Layout**: Whitespace, visual hierarchy, padding. Text must not touch edges.

---

## Delivery Checklist

- [ ] Valid `.docx` opens without errors
- [ ] All user requirements completed, no placeholders
- [ ] Heading hierarchy and body styles consistent
- [ ] Footnotes link to real references (if applicable)
- [ ] Headers/footers present with page numbers

**Domain checks**: Contracts (party names, clause numbering, amounts consistent). Papers (abstract-body-references complete, citations match). Reports (KPIs consistent, risk section exists).

**Regression checks**: No accidentally deleted paragraphs, no duplicates, table merges intact.
