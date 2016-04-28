/**
* Name: "Wemo Switch
* Author: Cor Dikland
*	cdikland@gmail.com
* Date: 2015-04-13
*
* Create virtual on/off button devices for each of your wemo switches/sockets/etc...
* In the Label field use the OnEvent rule name you used within WemoServer. 
* (i.e. Wemoserver rule= OnEvent[Backdoor] set "Back Door Light" to toggle. In this case you would specify "BackDoor" in the label field.)
*/
definition(
name: "Wemo Switch",
namespace: "",
author: "cdikland",
description: "Turn on/off Wemo.",
category: "",
iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
section("WemoServer LAN Address and Port"){
input name: "ip", type: "text", title: "ip:port"
}
section("Wemo Switches to Control"){
input "switches", "capability.switch", title: "Wemo Switches", multiple: true, required: true, refreshAfterSelection:true
}

}

def installed(){
subscribe(switches, "switch.on", onHandler)
subscribe(switches, "switch.off", offHandler)
}

def updated(){
unsubscribe()
subscribe(switches, "switch.on", onHandler)
subscribe(switches, "switch.off", offHandler)
}

def onHandler(evt) {
log.debug evt.value
log.info("Turning on Wemo ${evt.displayName}")
sendHttp("${evt.displayName}")
}

def offHandler(evt) {
log.debug evt.value
log.info("Turning off Wemo ${evt.displayName}")
sendHttp("${evt.displayName}")
}

def sendHttp(rule) {
sendHubCommand(new physicalgraph.device.HubAction("""GET /event/$rule HTTP/1.1\r\nHOST: $ip\r\n\r\n""", physicalgraph.device.Protocol.LAN))
}