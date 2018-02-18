package courage.library.authserver.controller;

import courage.library.authserver.dto.Library;
import courage.library.authserver.dto.User;
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

import java.util.Map;

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
                                                       @RequestParam(value = "size", required = false) Integer size,
                                                       @RequestParam(value =  "all", required = false)
                                                                   Boolean all) {
        Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
        page = pageAttributes.get("page");
        size = pageAttributes.get("size");

        Page<Library> libraries;
        if (all == null || all == false) {
            libraries = this.libraryQuery.findLibraries(page, size);
        } else {
            libraries = this.libraryQuery.findAllLibraries(page, size);
        }
        if (page > libraries.getTotalPages()) {
            throw NotFoundException.create("Not Found: Page number does not exist");
        }
        return new ResponseEntity<>(libraries, HttpStatus.OK);
    }

    @ApiOperation(value="Get a library based on id")
    @RequestMapping(
            value = "/{libraryId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Library> getLibrary( @PathVariable("libraryId")String libraryId,
                                               @RequestParam(value =  "all", required = false) Boolean all ) {
        Library library;
       if (all == null || all == false) {
            library = this.libraryQuery.findLibraryById(libraryId);
        } else {
            library = this.libraryQuery.findAllLibraryById(libraryId);
        }

        if (library == null) {
            throw NotFoundException.create("Not Found: Library with id, {0} does not exist", libraryId);
        }
        return new ResponseEntity<>(library, HttpStatus.OK);
    }

    @ApiOperation(value="Get a librarians")
    @RequestMapping(
            value = "/{libraryId}/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<User>> getLibrarians(@PathVariable("libraryId")String libraryId,
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "size", required = false) Integer size
                                              ) {
        Map<String, Integer> pageAttributes = PageValidator.validatePageAndSize(page, size);
        page = pageAttributes.get("page");
        size = pageAttributes.get("size");

        Page<User> librarians = this.libraryQuery.findLibrarians(libraryId, page, size);
        if (page > librarians.getTotalPages()) {
            throw NotFoundException.create("Not Found: Page number does not exist");
        }
        return new ResponseEntity<>(librarians, HttpStatus.OK);
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
            throw NotFoundException.create("Not Found: Library with id, {0} does not exist", libraryId);
        }
        Library updateLibrary = this.libraryCommand.updateLibrary(library);
        return new ResponseEntity<>(updateLibrary, HttpStatus.OK);
    }

    @ApiOperation(value="Restore deleted library")
    @RequestMapping(
            value = "/{libraryId}/activate",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Library> restoreLibrary( @PathVariable("libraryId")String libraryId ) {
        this.libraryCommand.restoreLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.OK);
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
