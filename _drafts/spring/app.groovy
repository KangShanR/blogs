@RestController
class ThisWillActuallyRun {

    @RequestMapping("/")
    String home() {
        "<h1>This is homepage!</h1>"
    }

    @RequestMapping("/house")
    String house() {
        "<h1>This is house!</h1>"
    }


}