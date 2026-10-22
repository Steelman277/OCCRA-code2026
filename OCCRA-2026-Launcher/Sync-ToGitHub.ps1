param([string]$Destination = 'C:/Users/SedamD/Documents/GitHub/OCCRA-2026-Launcher')
$ErrorActionPreference = 'Stop'
$files = @('README.md', 'TUNING.md', '.gitignore', 'build.gradle', 'settings.gradle', 'gradlew', 'gradlew.bat', 'Sync-ToGitHub.ps1')
foreach ($directory in @('src', 'gradle', 'vendordeps', '.wpilib')) {
    $files += Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot $directory) -File -Recurse | ForEach-Object {
        [System.IO.Path]::GetRelativePath($PSScriptRoot, $_.FullName)
    }
}
foreach ($relative in $files) {
    $source = Join-Path $PSScriptRoot $relative
    $target = Join-Path $Destination $relative
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $target) | Out-Null
    Copy-Item -LiteralPath $source -Destination $target -Force
    if ((Get-FileHash -LiteralPath $source).Hash -ne (Get-FileHash -LiteralPath $target).Hash) {
        throw "Verification failed: $relative"
    }
}
# Remove only retired constants sources, now consolidated into Constants.java.
foreach ($name in @('IntakeConstants.java', 'TransporterConstants.java', 'UptakeConstants.java', 'LauncherConstants.java')) {
    $relative = 'src/main/java/frc/robot/' + $name
    $oldSource = Join-Path $PSScriptRoot $relative
    $oldTarget = Join-Path $Destination $relative
    if (!(Test-Path -LiteralPath $oldSource) -and (Test-Path -LiteralPath $oldTarget)) {
        Remove-Item -LiteralPath $oldTarget -ErrorAction Stop
    }
}
Write-Output "Verified $($files.Count) files in $Destination. No commit or push performed."





