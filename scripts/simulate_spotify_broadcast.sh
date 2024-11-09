#!/bin/bash

# Function to check if adb is available
check_adb() {
    if ! command -v adb &> /dev/null; then
        echo "Error: ADB is not installed or not in PATH"
        exit 1
    fi
}

# Function to format current timestamp
get_timestamp() {
    date +%s
}

# Function to execute the Spotify command
run_spotify_command() {
    local iteration=$1
    local current_time=$(get_timestamp)

    adb shell "am broadcast -a com.spotify.music.metadatachanged \
        --es id 'spotify:track:7ALurdGTM0BZMHhUcrM2AW' \
        --el timeSent $current_time \
        --es artist 'Anonymouz' \
        --es album '11:11' \
        --es track 'River' \
        --ei length 198354"

    local status=$?
    if [ $status -eq 0 ]; then
        echo "Iteration $iteration/200 completed at $(date '+%H:%M:%S')"
    else
        echo "Error in iteration $iteration: Command failed with status $status"
    fi
}

# Main execution
echo "Starting Spotify ADB command loop..."
check_adb

for ((i=1; i<=200; i++)); do
    run_spotify_command $i

    sleep 0.5
done

echo -e "\nLoop completed successfully!"
