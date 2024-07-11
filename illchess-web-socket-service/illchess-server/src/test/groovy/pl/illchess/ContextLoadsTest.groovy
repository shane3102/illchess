package pl.illchess

class ContextLoadsTest extends SpecificationIT {
    def "context loads test"() {
        expect:
        mockMvc != null
    }
}
