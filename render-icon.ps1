# Renders the launcher icon (adaptive vector XML) to PNGs for the Play listing.
# Geometry and colors are parsed from the actual XML sources:
#   app/src/main/res/drawable/ic_launcher_foreground.xml
#   app/src/main/res/values/colors.xml
param([int[]]$Sizes = @(512, 1024))

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$vectorPath = Join-Path $root "app\src\main\res\drawable\ic_launcher_foreground.xml"
$colorsPath = Join-Path $root "app\src\main\res\values\colors.xml"
$outDir = Join-Path $root "store"

[xml]$vector = Get-Content $vectorPath
[xml]$colors = Get-Content $colorsPath

# --- parse from XML ---
$viewport = [double]$vector.vector.viewportWidth
$bgColorText = ($colors.resources.color | Where-Object { $_.name -eq "ic_launcher_background" }).InnerText.Trim()
$fgColorText = ($vector.vector.path | Select-Object -First 1).fillColor

function Convert-Color([string]$hex) {
    $h = $hex.TrimStart("#")
    return [System.Drawing.Color]::FromArgb(
        [Convert]::ToInt32($h.Substring(0, 2), 16),
        [Convert]::ToInt32($h.Substring(2, 2), 16),
        [Convert]::ToInt32($h.Substring(4, 2), 16))
}

$bgColor = Convert-Color $bgColorText
$fgColor = Convert-Color $fgColorText

# circles from pathData: "M{x},{y} m-{r},0 a{r},{r} ..."
$circles = @()
foreach ($p in $vector.vector.path) {
    if ($p.pathData -match 'M(-?\d+),(-?\d+)\s+m-(\d+),0') {
        $circles += , @([double]$Matches[1], [double]$Matches[2], [double]$Matches[3])
    }
}
if ($circles.Count -eq 0) { throw "No circles found in $vectorPath" }

New-Item -ItemType Directory -Force -Path $outDir | Out-Null

foreach ($size in $Sizes) {
    $bmp = New-Object System.Drawing.Bitmap($size, $size)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.SmoothingMode = "AntiAlias"
    $g.InterpolationMode = "HighQualityBicubic"
    $g.Clear($bgColor)

    $scale = $size / $viewport
    $brush = New-Object System.Drawing.SolidBrush($fgColor)
    foreach ($c in $circles) {
        $x = ($c[0] - $c[2]) * $scale
        $y = ($c[1] - $c[2]) * $scale
        $d = 2 * $c[2] * $scale
        $g.FillEllipse($brush, [float]$x, [float]$y, [float]$d, [float]$d)
    }
    $g.Dispose()

    $outPath = Join-Path $outDir ("icon-{0}.png" -f $size)
    $bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    Write-Output ("wrote {0}  ({1}x{1}, bg={2}, circles={3})" -f $outPath, $size, $bgColorText, $circles.Count)
}
