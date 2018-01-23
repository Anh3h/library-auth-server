package courage.library.authserver.controller;

import courage.library.authserver.dto.Library;
import courage.library.authserver.exception.BadRequestException;
import courage.library.authserver.exception.NotFoundException;
import courage.library.authserver.service.command.LibraryCommand;
import courage.library.authserver.service.query.LibraryQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    @Autowired
    private LibraryQuery libraryQuery;

    @Autowired
    private LibraryCommand libraryCommand;

    @ApiOperation(value="Create new library")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Library> createLibrary( @RequestBody Library library ) {
        Library newLibrary = this.libraryCommand.createLibrary(library);
        return new ResponseEntity<>(newLibrary, HttpStatus.CREATED);
    }

    @ApiOperation(value="Get all/some libraries")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<Library>> getLibraries( @RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "size", required = false) Integer size ) {
        if( page == null || size == null ) {
            page = 1;
            size = 20;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Invalid page number: {0} or page size: {1} value", page, size);
        }

        Page<Library> libraries = this.libraryQuery.findLibraries(page, size);
        if (page > libraries.getTotalPages()) {
            throw NotFoundException.create("Page number does not exist");
        }
        return new ResponseEntity<>(libraries, HttpStatus.OK);
    }

    @ApiOperation(value="Get a library based on id")
    @RequestMapping(
            value = "/{libraryId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Library> getLibrary( @PathVariable("libraryId")String libraryId) {
        Library library = this.libraryQuery.findLibraryById(libraryId);
        if (library == null) {
            throw NotFoundException.create("Library with id, {0} does not exist", libraryId);
        }
        return new ResponseEntity<>(library, HttpStatus.OK);
    }

    @ApiOperation(value="Update a library based on id")
    @RequestMapping(
            value = "/{libraryId}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Library> updateLibrary( @RequestBody Library library,
                                                  @PathVariable("libraryId")String libraryId) {
        if ( this.libraryQuery.findLibraryById(libraryId) == null) {
            throw NotFoundException.create("Library with id, {0} does not exist", libraryId);
        }
        Library updateLibrary = this.libraryCommand.updateLibrary(library);
        return new ResponseEntity<>(updateLibrary, HttpStatus.OK);
    }

    @ApiOperation(value="Delete a library based on library_id")
    @RequestMapping(
            value = "/{libraryId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<HttpStatus> deleteLibrary( @PathVariable("libraryId")String libraryId ) {
        this.libraryCommand.deleteLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
