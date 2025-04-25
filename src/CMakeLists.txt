target_sources(
    ${CMAKE_PROJECT_NAME} PRIVATE

    main.cpp
)

set(FOLDERS
    OSSupport
)

# Add all child source directories:
foreach(folder ${FOLDERS})
    add_subdirectory(${folder})
endforeach(folder)

