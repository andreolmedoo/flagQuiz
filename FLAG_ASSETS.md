# Flag assets

The 271 flag images in `app/src/main/assets/flags/` come from the
[flag-icons 4:3 collection](https://github.com/lipis/flag-icons/tree/main/flags/4x3).
The original SVG files are kept there with the upstream MIT `LICENSE`.

Each SVG was rasterized to a 1200 x 900 RGBA PNG in
`app/src/main/res/drawable-nodpi/`. PNG names start with `flag_` and replace
hyphens with underscores to meet Android resource naming rules, including
country codes that are Java keywords. For example, `gb-sct.svg` becomes
`flag_gb_sct.png` and can be referenced as `R.drawable.flag_gb_sct`.
