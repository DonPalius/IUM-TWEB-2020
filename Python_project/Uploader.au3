#Region ;**** Directives created by AutoIt3Wrapper_GUI ****
#AutoIt3Wrapper_UseX64=y
#EndRegion ;**** Directives created by AutoIt3Wrapper_GUI ****
ControlFocus("Caricamento file", "", "Edit1")
ControlSetText("Caricamento file", "", "Edit1", $CmdLine[1])
ControlClick("Caricamento file", "", "Button1")